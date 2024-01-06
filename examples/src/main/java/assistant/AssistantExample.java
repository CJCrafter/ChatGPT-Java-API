package assistant;

import com.cjcrafter.openai.OpenAI;
import com.cjcrafter.openai.assistants.CreateAssistantRequest;
import com.cjcrafter.openai.assistants.ModifyAssistantRequest;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Scanner;

public class AssistantExample {

    // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
    // dependency. Then you can add a .env file in your project directory.
    public static final OpenAI openai = OpenAI.builder()
            .apiKey(Dotenv.load().get("OPENAI_TOKEN"))
            .build();

    public static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {

        int input;
        do {
            System.out.println("1. Create");
            System.out.println("2. Retrieve");
            System.out.println("3. List");
            System.out.println("4. Delete");
            System.out.println("5. Modify");
            System.out.println("6. Exit");

            System.out.print("Code: ");
            input = Integer.parseInt(scan.nextLine());
            switch (input) {
                case 1:
                    create();
                    break;
                case 2:
                    retrieve();
                    break;
                case 3:
                    list();
                    break;
                case 4:
                    delete();
                    break;
                case 5:
                    modify();
                    break;
                case 6:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid code!");
            }
        } while (input != 6);
    }

    public static void create() {
        System.out.print("Model: ");
        String model = scan.nextLine();
        System.out.print("Name: ");
        String name = scan.nextLine();
        System.out.print("Description: ");
        String description = scan.nextLine();
        System.out.print("Instructions: ");
        String instructions = scan.nextLine();

        CreateAssistantRequest request = CreateAssistantRequest.builder()
                .model(model)
                .name(name)
                .description(description)
                .instructions(instructions)
                .build();

        System.out.println("Request: " + request);
        System.out.println("Response: " + openai.getAssistants().create(request));
    }

    public static void retrieve() {
        System.out.print("ID: ");
        String id = scan.nextLine();

        System.out.println("Response: " + openai.getAssistants().retrieve(id));
    }

    public static void list() {
        System.out.println("Response: " + openai.getAssistants().list());
    }

    public static void delete() {
        System.out.print("ID: ");
        String id = scan.nextLine();

        System.out.println("Response: " + openai.getAssistants().delete(id));
    }


    public static void modify() {
        System.out.print("ID: ");
        String id = scan.nextLine();
        System.out.print("Name: ");
        String name = scan.nextLine();
        System.out.print("Description: ");
        String description = scan.nextLine();
        System.out.print("Instructions: ");
        String instructions = scan.nextLine();

        ModifyAssistantRequest request = ModifyAssistantRequest.builder()
                .name(name)
                .description(description)
                .instructions(instructions)
                .build();

        System.out.println("Request: " + request);
        System.out.println("Response: " + openai.getAssistants().modify(id, request));
    }
}
