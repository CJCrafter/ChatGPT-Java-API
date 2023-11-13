package com.cjcrafter.openai

import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

abstract class MockedTest {

    protected val mockWebServer = MockWebServer()
    protected lateinit var client: OkHttpClient

    @BeforeEach
    fun setUp() {
        mockWebServer.start()
        client = OkHttpClient.Builder().build()
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    fun readResource(resource: String): String {
        return this::class.java.classLoader.getResource(resource)?.readText() ?: throw Exception("Resource '$resource' not found")
    }
}