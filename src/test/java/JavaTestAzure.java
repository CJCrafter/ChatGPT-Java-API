import com.cjcrafter.openai.AzureOpenAI;
import com.cjcrafter.openai.OpenAI;
import com.cjcrafter.openai.chat.ChatMessage;
import com.cjcrafter.openai.chat.ChatRequest;
import com.cjcrafter.openai.chat.ChatResponse;
import com.cjcrafter.openai.completions.CompletionRequest;
import com.cjcrafter.openai.exception.OpenAIError;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class JavaTestAzure {

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

    public static void main(String[] args) throws OpenAIError {
        Scanner scanner = new Scanner(System.in);

        // Add test cases for AzureOpenAI
        System.out.println(GREEN + "    9. Azure Completion (create, sync)");
        System.out.println(GREEN + "    10. Azure Completion (stream, sync)");
        System.out.println(GREEN + "    11. Azure Completion (create, async)");
        System.out.println(GREEN + "    12. Azure Completion (stream, async)");
        System.out.println(GREEN + "    13. Azure Chat (create, sync)");
        System.out.println(GREEN + "    14. Azure Chat (stream, sync)");
        System.out.println(GREEN + "    15. Azure Chat (create, async)");
        System.out.println(GREEN + "    16. Azure Chat (stream, async)");
        System.out.println();

        // Determine which method to call
        switch (scanner.nextLine()) {
            // ...
            case "9":
                doCompletionAzure(false, false);
                break;
            case "10":
                doCompletionAzure(true, false);
                break;
            case "11":
                doCompletionAzure(false, true);
                break;
            case "12":
                doCompletionAzure(true, true);
                break;
            case "13":
                doChatAzure(false, false);
                break;
            case "14":
                doChatAzure(true, false);
                break;
            case "15":
                doChatAzure(false, true);
                break;
            case "16":
                doChatAzure(true, true);
                break;
            default:
                System.err.println("Invalid option");
                break;
        }
    }

    public static void doCompletionAzure(boolean stream, boolean async) throws OpenAIError {
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
        OpenAI openai = new AzureOpenAI(key);
        System.out.println(RESET + "Generating Response" + PURPLE);

        // Generate a print the message
        if (stream) {
            if (async)
                openai.streamCompletionAsync(request, response -> System.out.print(response.get(0).getText()));
            else
                openai.streamCompletion(request, response -> System.out.print(response.get(0).getText()));
        } else {
            if (async)
                openai.createCompletionAsync(request, response -> System.out.println(response.get(0).getText()));
            else
                System.out.println(openai.createCompletion(request).get(0).getText());
        }

        System.out.println(CYAN + "  !!! Code has finished executing. Wait for async code to complete." + RESET);
    }

    public static void doChatAzure(boolean stream, boolean async) throws OpenAIError {
        Scanner scan = new Scanner(System.in);

        // This is the prompt that the bot will refer back to for every message.
        ChatMessage prompt = ChatMessage.toSystemMessage("You are a helpful chatbot.");

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
        OpenAI openai = new AzureOpenAI(key);

        // The conversation lasts until the user quits the program
        while (true) {

            // Prompt the user to enter a response
            System.out.println(YELLOW + "Enter text below:\n\n");
            String input = scan.nextLine();

            // Add the newest user message to the conversation
            messages.add(ChatMessage.toUserMessage(input));

            System.out.println(RESET + "Generating Response" + PURPLE);
            if (stream) {
                if (async) {
                    openai.streamChatCompletionAsync(request, response -> {
                        System.out.print(response.get(0).getDelta());
                        if (response.get(0).isFinished())
                            messages.add(response.get(0).getMessage());
                    });
                } else {
                    openai.streamChatCompletion(request, response -> {
                        System.out.print(response.get(0).getDelta());
                        if (response.get(0).isFinished())
                            messages.add(response.get(0).getMessage());
                    });
                }
            } else {
                if (async) {
                    openai.createChatCompletionAsync(request, response -> {
                        System.out.println(response.get(0).getMessage().getContent());
                        messages.add(response.get(0).getMessage());
                    });
                } else {
                    ChatResponse response = openai.createChatCompletion(request);
                    System.out.println(response.get(0).getMessage().getContent());
                    messages.add(response.get(0).getMessage());
                }
            }

            System.out.println(CYAN + "  !!! Code has finished executing. Wait for async code to complete.");
        }
    }
}