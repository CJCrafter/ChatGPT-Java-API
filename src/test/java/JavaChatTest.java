import com.cjcrafter.openai.OpenAI;
import com.cjcrafter.openai.chat.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JavaChatTest {

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        String key = Dotenv.load().get("OPENAI_TOKEN");

        // Create the initial prompt, we will reuse it later.
        String initialPrompt = "You are a customer support chat-bot. Write brief summaries of the user's questions so that agents can easily find the answer in a database.";
        List<ChatMessage> messages = new ArrayList<>(List.of(new ChatMessage(ChatUser.SYSTEM, initialPrompt)));
        ChatRequest request = new ChatRequest("gpt-3.5-turbo", messages);
        OpenAI openai = new OpenAI(key);

        while (true) {
            System.out.println("Enter text below:\n\n");
            String input = scan.nextLine();

            // Generate a response, and print it to the user.
            messages.add(new ChatMessage(ChatUser.USER, input));
            ChatResponse response = openai.createChatCompletion(request);
            System.out.println("\n" + response.get(0).getMessage().getContent());

            // Save the generated message to the bot's conversational memory
            messages.add(response.get(0).getMessage());
        }
    }
}
