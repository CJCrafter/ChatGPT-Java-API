# ChatGPT-Java-API
This is an easy-to-use "drag and drop" API that allows you to use OpenAI's new ChatGPT API. This API
works by wrapping HTTPS requests with java variables, making the generated results much easier to control.

Feel free to use, modify, and distribute this code as needed.

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

# Installation
1. Add [okhttp](https://square.github.io/okhttp/) and [gson](https://github.com/google/gson) as dependencies (see below)
2. Drag and drop the [`ChatBot.java`](https://github.com/CJCrafter/ChatGPT-Java-API/blob/master/ChatBot.java) file into your project

Maven:
```xml
  <dependencies>
    <dependency>
      <groupId>com.squareup.okhttp3</groupId>
      <artifactId>okhttp</artifactId>
      <version>4.9.2</version>
    </dependency>
    <dependency>
      <groupId>com.google.code.gson</groupId>
      <artifactId>gson</artifactId>
      <version>2.8.9</version>
    </dependency>
  </dependencies>
```

Gradle KTS:
```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("com.google.code.gson:gson:2.8.9")
}
```

# More
1. I also wrote a [Kotlin Version]() of the API.
2. Need inspiration for prompts? Check out [awesome prompts](https://github.com/f/awesome-chatgpt-prompts).
3. Looking for the official API? OpenAI only officially supports [python](https://github.com/openai/openai-python).

# Support
If I have saved you time, please consider [sponsoring me](https://github.com/sponsors/CJCrafter). 
If you cannot financially support me, consider leaving a star on the repository and sharing it. Thanks!

# License
ChatGPT-Kotlin-API is licensed under the [MIT License](https://github.com/CJCrafter/ChatGPT-Java-API/blob/master/LICENSE).
