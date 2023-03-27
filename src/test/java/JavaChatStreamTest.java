import com.cjcrafter.openai.OpenAI;
import com.cjcrafter.openai.chat.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JavaChatStreamTest {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Prepare the ChatRequest
        ChatMessage prompt = ChatMessage.toSystemMessage("Be as unhelpful as possible");
        List<ChatMessage> messages = new ArrayList<>(List.of(prompt));
        ChatRequest request = ChatRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages).build();

        // Load TOKEN from .env file
        String key = Dotenv.load().get("OPENAI_TOKEN");
        OpenAI openai = new OpenAI(key);

        // Ask the user for input
        System.out.println("Enter text below:\n\n");
        String input = scan.nextLine();

        // Stream the response. Print out each 'delta' (new tokens)
        messages.add(new ChatMessage(ChatUser.USER, input));
        openai.streamChatCompletion(request, message -> {
            System.out.print(message.get(0).getDelta());

            // Once the message is complete, we should save the message to our
            // conversation (In case you want to generate more responses).
            if (message.get(0).getFinishReason() != null)
                messages.add(message.get(0).getMessage());
        });
    }
}
