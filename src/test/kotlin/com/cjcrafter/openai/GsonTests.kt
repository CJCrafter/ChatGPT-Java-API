package com.cjcrafter.openai

import com.cjcrafter.openai.chat.*
import com.cjcrafter.openai.chat.ChatMessage.Companion.toAssistantMessage
import com.cjcrafter.openai.chat.ChatMessage.Companion.toSystemMessage
import com.cjcrafter.openai.completions.CompletionRequest
import com.google.gson.Gson
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class GsonTests {

    private var gson: Gson? = null

    @BeforeEach
    fun setUp() {
        gson = OpenAI.createGson()
    }

    @AfterEach
    fun tearDown() {
        gson = null
    }

    @ParameterizedTest
    @MethodSource("provide_json")
    fun test_toJson(json: String, obj: Any, clazz: Class<Any>) {
        Assertions.assertEquals(json, gson!!.toJson(obj))
    }

    @ParameterizedTest
    @MethodSource("provide_json")
    fun test_fromJson(json: String, obj: Any, clazz: Class<Any>) {
        Assertions.assertEquals(obj, gson!!.fromJson(json, clazz))
    }

    companion object {
        @JvmStatic
        private fun provide_json(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(
                    "{\"model\":\"gpt-3.5-turbo\",\"messages\":[{\"role\":\"system\",\"content\":\"You are a ChatBot\"}]}",
                    ChatRequest(model="gpt-3.5-turbo", messages = mutableListOf("You are a ChatBot".toSystemMessage())),
                    ChatRequest::class.java
                ),
                Arguments.of(
                    "{\"id\":\"chatcmpl-123\",\"created\":1677652288,\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\"Hello there, how may I assist you today?\"},\"finish_reason\":\"stop\"}],\"usage\":{\"prompt_tokens\":9,\"completion_tokens\":12,\"total_tokens\":21}}",
                    ChatResponse("chatcmpl-123", 1677652288L, mutableListOf(ChatChoice(0, "Hello there, how may I assist you today?".toAssistantMessage(), FinishReason.STOP)), ChatUsage(9, 12, 21)),
                    ChatResponse::class.java
                ),
                Arguments.of(
                    "{\"model\":\"davinci\",\"prompt\":[\"Hello\",\"Goodbye\"]}",
                    CompletionRequest(model="davinci", prompt=listOf("Hello", "Goodbye")),
                    CompletionRequest::class.java
                )
            )
        }
    }
}