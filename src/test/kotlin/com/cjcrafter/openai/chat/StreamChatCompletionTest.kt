package com.cjcrafter.openai.chat

import com.cjcrafter.openai.MockedTest
import com.cjcrafter.openai.chat.ChatMessage.Companion.toSystemMessage
import com.cjcrafter.openai.chat.tool.ToolType
import com.cjcrafter.openai.openAI
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class StreamChatCompletionTest : MockedTest() {

    @Test
    fun `test stream`() {

        // Create a mock webserver with our 2 responses (see stream_chat_completion_1.txt and 2)
        mockWebServer.enqueue(MockResponse().setBody(readResource("stream_chat_completion_1.txt")))
        mockWebServer.enqueue(MockResponse().setBody(readResource("stream_chat_completion_2.txt")))

        // Create a new OpenAI instance with our mock webserver
        val openai = openAI {
            apiKey("sk-123456789")
            client(client)
            baseUrl(mockWebServer.url("/").toString())
        }

        val dummyRequest = chatRequest {
            model("gpt-3.5-turbo")
            addMessage("Help the user".toSystemMessage()) // this doesn't matter since we are using a mock webserver

            function {
                name("solve_math_problem")
                description("Does math")
                addStringParameter("equation", "The equation to evaluate", true)
            }
        }

        // This first stream will return a tool call. We will
        lateinit var toolMessage: ChatMessage
        for (chunk in openai.streamChatCompletion(dummyRequest)) {
            if (chunk[0].isFinished())
                toolMessage = chunk[0].message
        }

        // If this were a call to the actual api.openai.com endpoint, we would
        // be *required* to respond to the assistant tool request with a tool
        // message. Since we are using a mock webserver, we can skip that.

        // This second stream will return a message
        lateinit var message: ChatMessage
        for (chunk in openai.streamChatCompletion(dummyRequest)) {
            if (chunk[0].isFinished())
                message = chunk[0].message
        }

        // Assertions
        assertEquals(ChatUser.ASSISTANT, toolMessage.role, "Tool call should be from the assistant")
        assertEquals(ToolType.FUNCTION, toolMessage.toolCalls?.get(0)?.type, "Tool call should be a function")
        assertEquals("solve_math_problem", toolMessage.toolCalls?.get(0)?.function?.name)
        assertEquals("3/2", toolMessage.toolCalls?.get(0)?.function?.tryParseArguments()?.get("equation")?.asText())

        assertEquals(ChatUser.ASSISTANT, message.role, "Message should be from the assistant")
        assertEquals("The result of 3 divided by 2 is 1.5.", message.content)
        assertEquals(null, message.toolCalls)
        assertEquals(null, message.toolCallId)
    }
}