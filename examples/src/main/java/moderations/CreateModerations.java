package moderations;

import com.cjcrafter.openai.OpenAI;
import com.cjcrafter.openai.moderations.CreateModerationRequest;
import com.cjcrafter.openai.moderations.Moderation;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Comparator;
import java.util.Scanner;

public class CreateModerations {

    // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
    // dependency. Then you can add a .env file in your project directory.
    public static final OpenAI openai = OpenAI.builder()
            .apiKey(Dotenv.load().get("OPENAI_TOKEN"))
            .build();

    public static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.print("Input: ");
            String input = scan.nextLine();
            CreateModerationRequest request = CreateModerationRequest.builder()
                    .input(input)
                    .build();

            Moderation moderation = openai.moderations().create(request);
            Moderation.Result result = moderation.getResults().get(0);

            // Finds the category with the highest score
            String highest = result.getCategoryScores().keySet().stream()
                    .max(Comparator.comparing(a -> result.getCategoryScores().get(a)))
                    .orElseThrow(() -> new RuntimeException("No categories found!"));

            System.out.println("Highest category: " + highest + ", with a score of " + result.getCategoryScores().get(highest));
        }
    }
}
