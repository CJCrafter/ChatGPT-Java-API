package com.cjcrafter.openai

import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class MockedTest {

    protected val mockWebServer = MockWebServer()
    protected lateinit var client: OkHttpClient
    protected lateinit var openai: OpenAI

    @BeforeEach
    fun setUp() {
        mockWebServer.start()
        client = OkHttpClient.Builder().build()
        openai = openAI {
            apiKey("sk-123456789")
            client(client)
            baseUrl(mockWebServer.url("/").toString())
        }
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    fun readResource(resource: String): String {
        return this::class.java.classLoader.getResource(resource)?.readText() ?: throw Exception("Resource '$resource' not found")
    }
}