import com.cjcrafter.openai.chat.ChatBot;
import com.cjcrafter.openai.chat.ChatMessage;
import com.cjcrafter.openai.chat.ChatRequest;
import com.cjcrafter.openai.chat.ChatResponse;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class JavaTest {

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        String key = getToken();

        // Create the initial prompt, we will reuse it later.
        String initialPrompt = "You are a customer support chat-bot. Write brief summaries of the user's questions so that agents can easily find the answer in a database.";
        List<ChatMessage> messages = List.of(new ChatMessage("system", initialPrompt));
        ChatRequest request = new ChatRequest("gpt-3.5-turbo", messages);
        ChatBot bot = new ChatBot(key);

        // ChatCompletionRequest copies the list, so let's modify the request's
        // copy of the list.
        messages = request.getMessages();

        while (true) {
            System.out.println("Enter text below:\n\n");
            String input = scan.nextLine();

            // Generate a response, and print it to the user.
            messages.add(new ChatMessage("user", input));
            ChatResponse response = bot.generateResponse(request);
            System.out.println("\n" + response.getChoices().get(0).getMessage().getContent());

            // Save the generated message to the bot's conversational memory
            messages.add(response.getChoices().get(0).getMessage());
        }
    }

    public static String getToken() {
        try (InputStream stream = JavaTest.class.getResourceAsStream("token.txt")) {
            Scanner scan = new Scanner(stream);
            return scan.nextLine();
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }
}
