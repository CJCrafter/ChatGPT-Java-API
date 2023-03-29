# ChatGPT-Java-API
This is an easy-to-use "drag and drop" API that allows you to use OpenAI's new ChatGPT API. This API
works by wrapping HTTPS requests with java variables, making the generated results much easier to control.

Feel free to use, modify, and distribute this code as needed.

# Installation
For Kotlin DSL (`build.gradle.kts`), add this to your dependencies block:
```kotlin
dependencies {
    implementation("com.cjcrafter:openai:1.2.5")
}
```
For Maven projects, add this to your `pom.xml` file in the `<dependencies>` block:
```xml
<dependency>
    <groupId>com.cjcrafter</groupId>
    <artifactId>openai</artifactId>
    <version>1.2.5</version>
</dependency>
```
See the [maven repository](https://central.sonatype.com/artifact/com.cjcrafter/openai/1.2.5) for gradle/ant/etc.


# Working Example
Check the wiki for more examples.
```java
public class JavaChatTest {

    public static void main(String[] args) throws OpenAIError {
        Scanner scan = new Scanner(System.in);

        // This is the prompt that the bot will refer back to for every message.
        ChatMessage prompt = ChatMessage.toSystemMessage("You are a customer support chat-bot. Write brief summaries of the user's questions so that agents can easily find the answer in a database.");

        // Use a mutable (modifiable) list! Always! You should be reusing the
        // ChatRequest variable, so in order for a conversation to continue
        // you need to be able to modify the list.
        List<ChatMessage> messages = new ArrayList<>(List.of(prompt));

        // ChatRequest is the request we send to OpenAI API. You can modify the
        // model, temperature, maxTokens, etc. This should be saved, so you can
        // reuse it for a conversation.
        ChatRequest request = ChatRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(messages).build();

        // Loads the API key from the .env file in the root directory.
        String key = Dotenv.load().get("OPENAI_TOKEN");
        OpenAI openai = new OpenAI(key);

        // The conversation lasts until the user quits the program
        while (true) {

            // Prompt the user to enter a response
            System.out.println("Enter text below:\n\n");
            String input = scan.nextLine();

            // Add the newest user message to the conversation
            messages.add(ChatMessage.toUserMessage(input));

            // Use the OpenAI API to generate a response to the current
            // conversation. Print the resulting message.
            ChatResponse response = openai.createChatCompletion(request);
            System.out.println("\n" + response.get(0).getMessage().getContent());

            // Save the generated message to the conversational memory. It is
            // crucial to save this message, otherwise future requests will be
            // confused that there was no response.
            messages.add(response.get(0).getMessage());
        }
    }
}

```

# Support
If I have saved you time, please consider [sponsoring me](https://github.com/sponsors/CJCrafter). 
If you cannot financially support me, consider leaving a star on the repository and sharing it. Thanks!

# License
ChatGPT-Java-API is licensed under the [MIT License](https://github.com/CJCrafter/ChatGPT-Java-API/blob/master/LICENSE).
