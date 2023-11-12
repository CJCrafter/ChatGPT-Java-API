<div align="center">

# ChatGPT Java API
  [![Maven Central](https://img.shields.io/maven-central/v/com.cjcrafter/openai?color=blue&label=Download)](https://central.sonatype.com/namespace/com.cjcrafter)
  [![](https://img.shields.io/badge/-docs%20-blueviolet?logo=Kotlin&colorA=gray)](https://openai.cjcrafter.com/)
  [![](https://img.shields.io/badge/-examples%20-orange?logo=Read+The+Docs&colorA=gray)](https://github.com/CJCrafter/ChatGPT-Java-API/tree/master/examples/src/main)
  [![](https://img.shields.io/github/discussions/CJCrafter/ChatGPT-Java-API)](https://github.com/CJCrafter/ChatGPT-Java-API/discussions)
  [![License](https://img.shields.io/github/license/CJCrafter/ChatGPT-Java-API)](https://github.com/CJCrafter/ChatGPT-Java-API/blob/master/LICENSE)

An unofficial, easy-to-use Java/Kotlin OpenAI API for ChatGPT, Text Completions, and more!
</div>

## Features
* [Completions](https://platform.openai.com/docs/api-reference/completions)
  * Streaming support via `OpenAI#streamCompletion`
* [Chat Completions](https://platform.openai.com/docs/api-reference/chat)
  * Streaming support via `OpenAI#streamChatCompletion`
  * Functions support, check out the [java examples](https://github.com/CJCrafter/ChatGPT-Java-API/blob/master/examples/src/main/java/chat/StreamChatCompletionFunction.java#L49) and [kotlin examples](https://github.com/CJCrafter/ChatGPT-Java-API/blob/master/examples/src/main/kotlin/chat/StreamChatCompletionFunction.kt#L37)
  * [Azure OpenAI](https://learn.microsoft.com/en-us/azure/cognitive-services/openai/reference) support via `AzureOpenAI` class

## Installation
For Kotlin DSL (`build.gradle.kts`), add this to your dependencies block:
```kotlin
dependencies {
    implementation("com.cjcrafter:openai:2.0.1")
}
```
For Maven projects, add this to your `pom.xml` file in the `<dependencies>` block:
```xml
<dependency>
    <groupId>com.cjcrafter</groupId>
    <artifactId>openai</artifactId>
    <version>2.0.1</version>
</dependency>
```
See the [maven repository](https://central.sonatype.com/artifact/com.cjcrafter/openai/2.0.1) for gradle/ant/etc.


## Working Example
This is a simple working example of the ChatGPT API in Java:
```java

import com.cjcrafter.openai.OpenAI;
import com.cjcrafter.openai.chat.ChatMessage;
import com.cjcrafter.openai.chat.ChatRequest;
import com.cjcrafter.openai.chat.ChatResponse;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * In this Java example, we will be using the Chat API to create a simple chatbot.
 */
public class ChatCompletion {

  public static void main(String[] args) {

    // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
    // dependency. Then you can add a .env file in your project directory.
    String key = Dotenv.load().get("OPENAI_TOKEN");
    OpenAI openai = OpenAI.builder()
            .apiKey(key)
            .build();

    List<ChatMessage> messages = new ArrayList<>();
    messages.add(ChatMessage.toSystemMessage("Help the user with their problem."));

    // Here you can change the model's settings, add tools, and more.
    ChatRequest request = ChatRequest.builder()
            .model("gpt-3.5-turbo")
            .messages(messages)
            .build();

    Scanner scan = new Scanner(System.in);
    while (true) {
      System.out.println("What are you having trouble with?");
      String input = scan.nextLine();

      messages.add(ChatMessage.toUserMessage(input));
      ChatResponse response = openai.createChatCompletion(request);

      System.out.println("Generating Response...");
      System.out.println(response.get(0).getMessage().getContent());

      // Make sure to add the response to the messages list!
      messages.add(response.get(0).getMessage());
    }
  }
}
```

For more examples, check out [examples](https://github.com/CJCrafter/ChatGPT-Java-API/tree/master/examples/src/main). 

> **Note**: OpenAI recommends using environment variables for your API token 
([Read more](https://help.openai.com/en/articles/5112595-best-practices-for-api-key-safety)).

## Logging
We use [SLF4J](http://www.slf4j.org/) for logging. To enable logging, add a logging implementation to your project.
If you encounter an issue with the JSON parsing, we will ask that you enable logging and send us the logs.

Adding a logging implementation:
```kotlin
implementation("ch.qos.logback:logback-classic:$version")
``` 

Add a `logback.xml` file to your resources folder:
```xml
<configuration>
    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>debug.log</file>
        <append>false</append>
        <encoder>
            <pattern>%date %level [%thread] %logger{10} %msg%n</pattern>
        </encoder>
    </appender>

    <logger name="com.cjcrafter.openai" level="DEBUG"/> <!-- Change to OFF to disable our logging -->

    <root level="DEBUG">
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```

## Support
If I have saved you time, please consider [sponsoring me](https://github.com/sponsors/CJCrafter).

## License
ChatGPT-Java-API is an open-sourced software licensed under the [MIT License](https://github.com/CJCrafter/ChatGPT-Java-API/blob/master/LICENSE).
**This is an unofficial library, and is not affiliated with OpenAI**.