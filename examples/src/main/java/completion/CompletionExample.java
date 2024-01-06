package completion;

import com.cjcrafter.openai.OpenAI;
import com.cjcrafter.openai.completions.CompletionRequest;
import io.github.cdimascio.dotenv.Dotenv;

/**
 * In this Java example, we will be using the Legacy Completion API to generate
 * a response.
 */
public class CompletionExample {

    public static void main(String[] args) {

        // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
        // dependency. Then you can add a .env file in your project directory.
        String key = Dotenv.load().get("OPENAI_TOKEN");
        OpenAI openai = OpenAI.builder()
                .apiKey(key)
                .build();

        String input = "What is 9+10?";

        // We use 128 tokens, so we have minimal delay before the response (for testing).
        CompletionRequest request = CompletionRequest.builder()
                .model("davinci")
                .prompt(input)
                .maxTokens(128).build();

        System.out.println("Generating Response...");
        String text = openai.createCompletion(request).get(0).getText();
        System.out.println(text);
    }
}
