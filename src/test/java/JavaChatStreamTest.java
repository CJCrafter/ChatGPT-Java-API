import com.cjcrafter.openai.chat.*;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JavaChatStreamTest {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String key = Dotenv.load().get("OPENAI_TOKEN");

        // Create the initial prompt, we will reuse it later.
        String initialPrompt = "You are a customer support chat-bot. Write brief summaries of the user's questions so that agents can easily find the answer in a database.";
        List<ChatMessage> messages = new ArrayList<>(List.of(new ChatMessage(ChatUser.SYSTEM, initialPrompt)));
        ChatRequest request = new ChatRequest("gpt-3.5-turbo", messages);
        ChatBot bot = new ChatBot(key);

        while (true) {
            System.out.println("Enter text below:\n\n");
            String input = scan.nextLine();

            // Generate a response, and print it to the user.
            messages.add(new ChatMessage(ChatUser.USER, input));
            bot.streamResponse(request, message -> {
                System.out.print(message.get(0).getDelta());

                if (message.get(0).getFinishReason() != null) {
                    messages.add(message.get(0).getMessage());
                }
            });
        }
    }
}
