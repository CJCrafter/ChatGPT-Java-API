package chat;

import com.cjcrafter.openai.OpenAI;
import com.cjcrafter.openai.chat.ChatMessage;
import com.cjcrafter.openai.chat.ChatRequest;
import com.cjcrafter.openai.chat.ChatResponse;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * In this Java example, we will be using the Chat API to create a simple chatbot.
 */
public class ChatCompletion {

    public static void main(String[] args) {

        // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
        // dependency. Then you can add a .env file in your project directory.
        String key = Dotenv.load().get("OPENAI_TOKEN");
        OpenAI openai = OpenAI.builder()
                .apiKey(key)
                .build();

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
            ChatResponse response = openai.createChatCompletion(request);

            System.out.println("Generating Response...");
            System.out.println(response.get(0).getMessage().getContent());

            // Make sure to add the response to the messages list!
            messages.add(response.get(0).getMessage());
        }
    }
}
