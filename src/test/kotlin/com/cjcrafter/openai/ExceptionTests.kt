package com.cjcrafter.openai

import com.cjcrafter.openai.chat.ChatMessage.Companion.toSystemMessage
import com.cjcrafter.openai.chat.ChatRequest
import com.cjcrafter.openai.exception.InvalidRequestError
import io.github.cdimascio.dotenv.Dotenv
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ExceptionTests {

    @Test
    fun test_invalidModel() {
        val key = Dotenv.load()["OPENAI_TOKEN"]
        val messages = mutableListOf("Just say hi".toSystemMessage())
        val request = ChatRequest.builder()
            .model("gpt-238974-invalid-model")
            .messages(messages)
            .build()
        val openai = OpenAIImpl(key)
        Assertions.assertThrows(InvalidRequestError::class.java) { openai.createChatCompletion(request) }
    }

    @Test
    fun test_invalidToken() {
        val key = "sk-Thisisaninvalidtoken"
        val messages = mutableListOf("Just say hi".toSystemMessage())
        val request = ChatRequest.builder()
            .model("gpt-3.5-turbo")
            .messages(messages)
            .build()
        val openai = OpenAIImpl(key)
        Assertions.assertThrows(InvalidRequestError::class.java) { openai.createChatCompletion(request) }
    }
}