package chat;

import com.cjcrafter.openai.OpenAI;
import com.cjcrafter.openai.chat.ChatMessage;
import com.cjcrafter.openai.chat.ChatRequest;
import com.cjcrafter.openai.chat.ChatResponseChunk;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * In this Java example, we will be using the Chat API to create a simple chatbot.
 * Instead of waiting for the full response to generate, we will "stream" tokens
 * 1 by 1 as they are generated.
 */
public class StreamChatCompletionExample {

    public static void main(String[] args) {

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
                .build();

        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("What are you having trouble with?");
            String input = scan.nextLine();

            messages.add(ChatMessage.toUserMessage(input));
            System.out.println("Generating Response...");

            for (ChatResponseChunk chunk : openai.streamChatCompletion(request)) {
                // This is nullable! ChatGPT will return null AT LEAST ONCE PER MESSAGE.
                String delta = chunk.get(0).getDeltaContent();
                if (delta != null)
                    System.out.print(delta);

                // When the response is finished, we can add it to the messages list.
                if (chunk.get(0).isFinished())
                    messages.add(chunk.get(0).getMessage());
            }

            // Print a new line to separate the messages
            System.out.println();
        }
    }
}
