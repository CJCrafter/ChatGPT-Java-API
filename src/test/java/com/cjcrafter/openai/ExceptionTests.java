package com.cjcrafter.openai;

import com.cjcrafter.openai.chat.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionTests {

    @Test
    void test_invalidModel() {
        String key = Dotenv.load().get("OPENAI_TOKEN");

        String initialPrompt = "Just say hi";
        List<ChatMessage> messages = new ArrayList<>(List.of(new ChatMessage(ChatUser.SYSTEM, initialPrompt)));
        ChatRequest request = new ChatRequest("gpt-238974-invalid-model", messages);
        ChatBot bot = new ChatBot(key);

        assertThrows(Throwable.class, () -> bot.generateResponse(request));
    }

    @Test
    void test_invalidToken() {
        String key = "sk-Thisisaninvalidtoken";

        String initialPrompt = "Just say hi";
        List<ChatMessage> messages = new ArrayList<>(List.of(new ChatMessage(ChatUser.SYSTEM, initialPrompt)));
        ChatRequest request = new ChatRequest("gpt-3.5-turbo", messages);
        ChatBot bot = new ChatBot(key);

        assertThrows(Throwable.class, () -> bot.generateResponse(request));
    }
}
