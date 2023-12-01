package chat;

import com.cjcrafter.openai.OpenAI;
import com.cjcrafter.openai.chat.ChatMessage;
import com.cjcrafter.openai.chat.ChatRequest;
import com.cjcrafter.openai.chat.ChatResponseChunk;
import com.cjcrafter.openai.chat.ChatUser;
import com.cjcrafter.openai.chat.tool.*;
import com.cjcrafter.openai.exception.HallucinationException;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.cdimascio.dotenv.Dotenv;
import org.mariuszgromada.math.mxparser.Expression;
import org.mariuszgromada.math.mxparser.License;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * In this Java example, we will be using the Chat API to create a simple chatbot.
 * Instead of waiting for the full response to generate, we will "stream" tokens
 * 1 by 1 as they are generated. We will also add a Math tool so that the chatbot
 * can solve math problems with a math parser.
 */
public class StreamChatCompletionFunction {

    public static void main(String[] args) {

        // Use mXparser
        License.iConfirmNonCommercialUse("CJCrafter");

        // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
        // dependency. Then you can add a .env file in your project directory.
        String key = Dotenv.load().get("OPENAI_TOKEN");
        OpenAI openai = OpenAI.builder()
                .apiKey(key)
                .build();

        // Notice that this is a *mutable* list. We will be adding messages later
        // so we can continue the conversation.
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(ChatMessage.toSystemMessage("Help the user with their problem."));

        // Here you can change the model's settings, add tools, and more.
        ChatRequest request = ChatRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .addTool(Function.builder()
                        .name("solve_math_problem")
                        .description("Returns the result of a math problem as a double")
                        .addStringParameter("equation", "The math problem for you to solve", true)
                        .build()
                )
                .build();

        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("What are you having trouble with?");
            String input = scan.nextLine();

            messages.add(ChatMessage.toUserMessage(input));
            System.out.println("Generating Response...");

            boolean madeToolCall;
            do {
                madeToolCall = false;
                for (ChatResponseChunk chunk : openai.streamChatCompletion(request)) {
                    String delta = chunk.get(0).getDeltaContent();
                    if (delta != null)
                        System.out.print(delta);

                    // When the response is finished, we can add it to the messages list.
                    if (chunk.get(0).isFinished())
                        messages.add(chunk.get(0).getMessage());
                }

                // If the API returned a tool call to us, we need to handle it.
                List<ToolCall> toolCalls = messages.get(messages.size() - 1).getToolCalls();
                if (toolCalls != null) {
                    madeToolCall = true;
                    for (ToolCall call : toolCalls) {
                        ChatMessage response = handleToolCall(call, request.getTools());
                        messages.add(response);
                    }
                }

                // Loop until we get a message without tool calls
            } while (madeToolCall);

            // Print a new line to separate the messages
            System.out.println();
        }
    }

    public static ChatMessage handleToolCall(ToolCall call, List<Tool> validTools) {
        // The try-catch here is *crucial*. ChatGPT *isn't very good*
        // at tool calls (And you probably aren't very good at prompt
        // engineering yet!). OpenAI will often "Hallucinate" arguments.
        try {
            if (call.getType() != Tool.Type.FUNCTION)
                throw new HallucinationException("Unknown tool call type: " + call.getType());

            FunctionCall function = call.getFunction();
            Map<String, JsonNode> arguments = function.tryParseArguments(validTools); // You can pass null here for less strict parsing
            String equation = arguments.get("equation").asText();
            double result = solveEquation(equation);

            // NaN implies that the equation was invalid
            if (Double.isNaN(result))
                throw new HallucinationException("Format was invalid: " + equation);

            // Add the result to the messages list
            String json = "{\"result\": " + result + "}";
            return new ChatMessage(ChatUser.TOOL, json, null, call.getId());

        } catch (HallucinationException ex) {

            // Lets let ChatGPT know it made a mistake so it can correct itself
            String json = "{\"error\": \"" + ex.getMessage() + "\"}";
            return new ChatMessage(ChatUser.TOOL, json, null, call.getId());
        }
    }

    public static double solveEquation(String equation) {
        Expression expression = new Expression(equation);
        return expression.calculate();
    }
}
