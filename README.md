<div align="center">

# ChatGPT Java API
  [![Maven Central](https://img.shields.io/maven-central/v/com.cjcrafter/openai?color=blue&label=Download)](https://central.sonatype.com/namespace/com.cjcrafter)
  [![](https://img.shields.io/badge/-docs%20-blueviolet?logo=Kotlin&colorA=gray)](https://openai.cjcrafter.com/)
  [![](https://img.shields.io/badge/-examples%20-orange?logo=Read+The+Docs&colorA=gray)](https://github.com/CJCrafter/ChatGPT-Java-API/wiki)
  [![](https://img.shields.io/github/discussions/CJCrafter/ChatGPT-Java-API)](https://github.com/CJCrafter/ChatGPT-Java-API/discussions)
  [![License](https://img.shields.io/github/license/WeaponMechanics/ArmorMechanics)](https://github.com/WeaponMechanics/ArmorMechanics/blob/master/LICENSE)

A community-maintained easy-to-use Java/Kotlin OpenAI API for ChatGPT, Text Completions, and more!
</div>

## Features
* [Completions](https://platform.openai.com/docs/api-reference/completions)
* [Chat Completions](https://platform.openai.com/docs/api-reference/chat)

## Installation
For Kotlin DSL (`build.gradle.kts`), add this to your dependencies block:
```kotlin
dependencies {
    implementation("com.cjcrafter:openai:1.3.0")
}
```
For Maven projects, add this to your `pom.xml` file in the `<dependencies>` block:
```xml
<dependency>
    <groupId>com.cjcrafter</groupId>
    <artifactId>openai</artifactId>
    <version>1.3.0</version>
</dependency>
```
See the [maven repository](https://central.sonatype.com/artifact/com.cjcrafter/openai/1.3.0) for gradle/ant/etc.


## Working Example
This is a basic working example. To see more features in action (async calls, streaming)
see the [java examples](https://github.com/CJCrafter/ChatGPT-Java-API/wiki/Java)
and [kotlin examples](https://github.com/CJCrafter/ChatGPT-Java-API/wiki/Kotlin)
```java
public class JavaChatTest {

    public static void main(String[] args) throws OpenAIError {
        Scanner scan = new Scanner(System.in);

        // This is the prompt that the bot will refer back to for every message.
        ChatMessage prompt = ChatMessage.toSystemMessage("You are ChatGPT, a helpful chat bot.");

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
        // You should never put your API keys in code, keep your key safe!
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
> **Note**: OpenAI recommends using environment variables for your API token 
([Read more](https://help.openai.com/en/articles/5112595-best-practices-for-api-key-safety)).

## Support
If I have saved you time, please consider [sponsoring me](https://github.com/sponsors/CJCrafter).

## License
ChatGPT-Java-API is an open-sourced software licensed under the [MIT License](https://github.com/CJCrafter/ChatGPT-Java-API/blob/master/LICENSE).
**This is an unofficial library, and is not affiliated with OpenAI**.