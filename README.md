# ChatGPT-Java-API
This is an easy-to-use "drag and drop" API that allows you to use OpenAI's new ChatGPT API. This API
works by wrapping HTTPS requests with java variables, making the generated results much easier to control.

Feel free to use, modify, and distribute this code as needed.

# Installation
For Kotlin DSL (`build.gradle.kts`), add this to your dependencies block:
```kotlin
dependencies {
    implementation("com.cjcrafter:openai:1.2.3")
}
```
For Maven projects, add this to your `pom.xml` file in the `<dependencies>` block:
```xml
<dependency>
    <groupId>com.cjcrafter</groupId>
    <artifactId>openai</artifactId>
    <version>1.2.3</version>
</dependency>
```
See the [maven repository](https://central.sonatype.com/artifact/com.cjcrafter/openai/1.2.3) for gradle/ant/etc.


# Working Example
```java
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        String key = "sk-YOUR KEY HERE";  // TODO Add your open ai key here
 
        // Create the initial prompt, we will reuse it later.
        String initialPrompt = "You are a customer support chat-bot. Write brief summaries of the user's questions so that agents can easily find the answer in a database.";
        List<ChatBot.ChatMessage> messages = List.of(new ChatBot.ChatMessage("system", initialPrompt));
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

            // Save the generated message to the bot's conversational memory
            messages.add(response.getChoices().get(0).getMessage());
        }
    }
}
```

# Support
If I have saved you time, please consider [sponsoring me](https://github.com/sponsors/CJCrafter). 
If you cannot financially support me, consider leaving a star on the repository and sharing it. Thanks!

# License
ChatGPT-Java-API is licensed under the [MIT License](https://github.com/CJCrafter/ChatGPT-Java-API/blob/master/LICENSE).
