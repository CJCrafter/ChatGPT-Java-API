import com.cjcrafter.openai.OpenAIImpl;
import com.cjcrafter.openai.chat.ChatMessage;
import com.cjcrafter.openai.chat.ChatRequest;
import com.cjcrafter.openai.chat.ChatResponse;
import com.cjcrafter.openai.chat.ChatResponseChunk;
import com.cjcrafter.openai.completions.CompletionRequest;
import com.cjcrafter.openai.completions.CompletionResponseChunk;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class JavaTest {

    // Colors for pretty formatting
    public static final String RESET = "\033[0m";
    public static final String BLACK = "\033[0;30m";
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Print out the menu of options
        System.out.println(GREEN + "Please select one of the options below by typing a number.");
        System.out.println();
        System.out.println(GREEN + "    1. Completion (create, sync)");
        System.out.println(GREEN + "    2. Completion (stream, sync)");
        System.out.println(GREEN + "    3. Chat (create, sync)");
        System.out.println(GREEN + "    4. Chat (stream, sync)");
        System.out.println();

        // Determine which method to call
        switch (scanner.nextLine()) {
            case "1":
                doCompletion(false);
                break;
            case "2":
                doCompletion(true);
                break;
            case "3":
                doChat(false);
                break;
            case "4":
                doChat(true);
                break;
            default:
                System.err.println("Invalid option");
                break;
        }
    }

    public static void doCompletion(boolean stream) {
        Scanner scan = new Scanner(System.in);
        System.out.println(YELLOW + "Enter completion: ");
        String input = scan.nextLine();

        // CompletionRequest contains the data we sent to the OpenAI API. We use
        // 128 tokens, so we have a bit of a delay before the response (for testing).
        CompletionRequest request = CompletionRequest.builder()
                .model("davinci")
                .prompt(input)
                .maxTokens(128).build();

        // Loads the API key from the .env file in the root directory.
        String key = Dotenv.load().get("OPENAI_TOKEN");
        OpenAIImpl openai = new OpenAIImpl(key);
        System.out.println(RESET + "Generating Response" + PURPLE);

        // Generate a print the message
        if (stream) {
            for (CompletionResponseChunk chunk : openai.streamCompletion(request)) {
                System.out.print(chunk.get(0).getText());
            }
        } else {
            System.out.println(openai.createCompletion(request).get(0).getText());
        }
    }

    public static void doChat(boolean stream) {
        Scanner scan = new Scanner(System.in);

        // This is the prompt that the bot will refer back to for every message.
        ChatMessage prompt = ChatMessage.toSystemMessage("You are a customer support chat-bot. Write brief summaries of the user's questions so that agents can easily find the answer in a database.");

        // Use a mutable (modifiable) list! Always! You should be reusing the
        // ChatRequest variable, so in order for a conversation to continue
        // you need to be able to modify the list.
        List<ChatMessage> messages = new ArrayList<>(Collections.singletonList(prompt));

        // ChatRequest is the request we send to OpenAI API. You can modify the
        // model, temperature, maxTokens, etc. This should be saved, so you can
        // reuse it for a conversation.
        ChatRequest request = ChatRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages).build();

        // Loads the API key from the .env file in the root directory.
        String key = Dotenv.load().get("OPENAI_TOKEN");
        OpenAIImpl openai = new OpenAIImpl(key);

        // The conversation lasts until the user quits the program
        while (true) {

            // Prompt the user to enter a response
            System.out.println(YELLOW + "Enter text below:\n\n");
            String input = scan.nextLine();

            // Add the newest user message to the conversation
            messages.add(ChatMessage.toUserMessage(input));

            System.out.println(RESET + "Generating Response" + PURPLE);
            if (stream) {
                for (ChatResponseChunk chunk : openai.streamChatCompletion(request)) {
                    System.out.print(chunk.get(0).getDelta());
                    if (chunk.get(0).isFinished())
                        messages.add(chunk.get(0).getMessage());
                }
            } else {
                ChatResponse response = openai.createChatCompletion(request);
                System.out.println(response.get(0).getMessage().getContent());
                messages.add(response.get(0).getMessage());
            }
        }
    }
}