package assistant;

import com.cjcrafter.openai.OpenAI;
import com.cjcrafter.openai.assistants.Assistant;
import com.cjcrafter.openai.assistants.ListAssistantResponse;
import com.cjcrafter.openai.threads.Thread;
import com.cjcrafter.openai.threads.message.*;
import com.cjcrafter.openai.threads.runs.CreateRunRequest;
import com.cjcrafter.openai.threads.runs.MessageCreationDetails;
import com.cjcrafter.openai.threads.runs.Run;
import com.cjcrafter.openai.threads.runs.RunStep;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Scanner;

public class ThreadExample {

    public static void main(String[] args) throws InterruptedException {
        // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
        // dependency. Then you can add a .env file in your project directory.
        OpenAI openai = OpenAI.builder()
                .apiKey(Dotenv.load().get("OPENAI_TOKEN"))
                .build();

        // Ask the user to choose an assistant
        ListAssistantResponse assistants = openai.assistants().list();
        for (int i = 0; i < assistants.getData().size(); i++) {
            Assistant assistant = assistants.getData().get(i);
            System.out.println(i + ". " + assistant);
        }

        Scanner scan = new Scanner(System.in);
        int choice = Integer.parseInt(scan.nextLine());
        Assistant assistant = assistants.getData().get(choice);

        // We have to create a new thread.  We'll save this thread, so we can
        // add user messages and get responses later.
        Thread thread = openai.threads().create();

        while (true) {

            // Handle user input
            System.out.println("Type your input below: ");
            String input = scan.nextLine();
            openai.threads().messages(thread).create(CreateThreadMessageRequest.builder()
                    .role(ThreadUser.USER)
                    .content(input)
                    .build());

            // After adding a message to the thread, we have to "run" the thread
            Run run = openai.threads().runs(thread).create(CreateRunRequest.builder()
                    .assistant(assistant)
                    .build());

            // This is a known limitation in OpenAI, and they are working to
            // address this so that we can easily stream a response without
            // nonsense like this.
            while (!run.getStatus().isTerminal()) {
                java.lang.Thread.sleep(1000);
                run = openai.threads().runs(thread).retrieve(run);
            }

            // Once the run stops, we want to retrieve the steps of the run.
            // this includes message outputs, function calls, code
            // interpreters, etc.
            for (RunStep step : openai.threads().runs(thread).steps(run).list().getData()) {
                if (step.getType() != RunStep.Type.MESSAGE_CREATION) {
                    System.out.println("Assistant made step: " + step.getType());
                    continue;
                }

                // This cast is safe since we checked the type above
                MessageCreationDetails details = (MessageCreationDetails) step.getStepDetails();
                ThreadMessage message = openai.threads().messages(thread).retrieve(details.getMessageCreation().getMessageId());
                for (ThreadMessageContent content : message.getContent()) {
                    if (content.getType() != ThreadMessageContent.Type.TEXT) {
                        System.err.println("Unhandled message content type: " + content.getType());
                        System.err.println("This will never occur since this Assistant doesn't use images.");
                        System.exit(-1);
                    }

                    System.out.println(((TextContent) content).getText().getValue());
                }
            }
        }
    }
}
