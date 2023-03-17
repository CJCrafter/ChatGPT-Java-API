import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        String key = "sk-YOUR KEY HERE";  // TODO Add your open ai key here
        
        // Create the initial prompt, we will reuse it later.
        String initialPrompt = "You are a customer support chat-bot. Write brief summaries of the user's questions so that agents can easily find the answer in a database.";
        
        //List<ChatBot.ChatMessage> messages = List.of(new ChatBot.ChatMessage("system", initialPrompt));
        List<ChatBot.ChatMessage> list = new ArrayList<>();
        list.add(new ChatBot.ChatMessage("system", initialPrompt));
        List<ChatBot.ChatMessage> messages = list;
        
        ChatBot.ChatCompletionRequest request = new ChatBot.ChatCompletionRequest("gpt-3.5-turbo", messages);
        ChatBot bot = new ChatBot(key);
        // ChatCompletionRequest copies the list, so let's modify the request's
        // copy of the list.
        messages = request.getMessages();

        while (true) {
            System.out.println("Enter text below:\n\n");
            String input = scan.nextLine();

            // Generate a response, and print it to the user.
            messages.add(new ChatBot.ChatMessage("user", input));
            ChatBot.ChatCompletionResponse response = bot.generateResponse(request);
            
            System.out.println("\n" + response.getChoices().get(0).getMessage().getContent());
            
            // Print our tokens consumption:
            System.out.println(response.getUsage() + "\n");
            

            // Save the generated message to the bot's conversational memory
            messages.add(response.getChoices().get(0).getMessage());
        }
    }
}