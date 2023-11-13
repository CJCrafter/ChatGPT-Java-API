package com.cjcrafter.openai.embeddings

import com.cjcrafter.openai.MockedTest
import com.cjcrafter.openai.openAI
import okhttp3.mockwebserver.MockResponse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Assertions.*

class MockedEmbeddingsTest : MockedTest() {

    @Test
    fun `test create embeddings list`() {
        mockWebServer.enqueue(MockResponse().setBody(readResource("create_embeddings.txt")))

        val dummyRequest = embeddingsRequest {
            input(listOf("Once upon a time", "There was a frog"))
            model("text-embedding-ada-002")
        }

        val response = openai.createEmbeddings(dummyRequest)
        response[0].asDoubles() // This will throw an exception if it is not a list of floats
        assertThrows<ClassCastException> {
            response[0].asBase64()
        }

        assertEquals(1, response.usage.promptTokens)
        assertEquals(1, response.usage.totalTokens)
    }
}