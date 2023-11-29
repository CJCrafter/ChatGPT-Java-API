package com.cjcrafter.openai.files

import com.cjcrafter.openai.MockedTest
import okhttp3.mockwebserver.MockResponse
import org.junit.jupiter.api.Test
import java.io.File

class MockedFilesTest : MockedTest() {

    @Test
    fun listFiles() {
        mockWebServer.enqueue(MockResponse().setBody(readResource("list_files.json")))

        val response = openai.files.list()

        // Intentionally empty... parsing to a valid response is the test
    }

    @Test
    fun uploadFile() {
        mockWebServer.enqueue(MockResponse().setBody(readResource("file.json")))

        val dummyRequest = uploadFileRequest {
            file(File("README.md"))
            purpose(FilePurpose.ASSISTANTS)
        }
        val response = openai.files.upload(dummyRequest)

        // Intentionally empty... parsing to a valid response is the test
    }

    @Test
    fun retrieveFile() {
        mockWebServer.enqueue(MockResponse().setBody(readResource("file.json")))

        val response = openai.files.retrieve("file-123abc")

        // Intentionally empty... parsing to a valid response is the test
    }

    @Test
    fun deleteFile() {
        mockWebServer.enqueue(MockResponse().setBody(readResource("delete_file.json")))

        val response = openai.files.delete("file-123abc")

        // Intentionally empty... parsing to a valid response is the test
    }
}