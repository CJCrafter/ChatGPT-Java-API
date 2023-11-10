package com.cjcrafter.openai

import com.cjcrafter.openai.chat.*
import com.cjcrafter.openai.completions.CompletionRequest
import com.cjcrafter.openai.completions.CompletionResponse
import com.cjcrafter.openai.completions.CompletionResponseChunk
import com.cjcrafter.openai.completions.CompletionUsage
import com.fasterxml.jackson.databind.node.ObjectNode
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.jetbrains.annotations.ApiStatus
import java.io.BufferedReader
import java.io.IOException

open class OpenAIImpl @ApiStatus.Internal constructor(
    protected val apiKey: String,
    protected val organization: String? = null,
    private val client: OkHttpClient = OkHttpClient()
): OpenAI {
    protected val mediaType = "application/json; charset=utf-8".toMediaType()
    protected val objectMapper = OpenAI.createObjectMapper()

    protected open fun buildRequest(request: Any, endpoint: String): Request {
        val json = objectMapper.writeValueAsString(request)
        val body: RequestBody = json.toRequestBody(mediaType)
        return Request.Builder()
            .url("https://api.openai.com/$endpoint")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .apply { if (organization != null) addHeader("OpenAI-Organization", organization) }
            .post(body).build()
    }

    override fun createCompletion(request: CompletionRequest): CompletionResponse {
        @Suppress("DEPRECATION")
        request.stream = false // use streamCompletion for stream=true
        val httpRequest = buildRequest(request, COMPLETIONS_ENDPOINT)

        val httpResponse = client.newCall(httpRequest).execute()
        println(httpResponse)

        return CompletionResponse("1", 1, "1", listOf(), CompletionUsage(1, 1, 1))
    }

    override fun streamCompletion(request: CompletionRequest): Iterable<CompletionResponseChunk> {
        @Suppress("DEPRECATION")
        request.stream = true // use createCompletion for stream=false
        val httpRequest = buildRequest(request, COMPLETIONS_ENDPOINT)

        return listOf()
    }

    override fun createChatCompletion(request: ChatRequest): ChatResponse {
        @Suppress("DEPRECATION")
        request.stream = false // use streamChatCompletion for stream=true
        val httpRequest = buildRequest(request, CHAT_ENDPOINT)

        val httpResponse = client.newCall(httpRequest).execute()
        if (!httpResponse.isSuccessful) {
            val json = httpResponse.body?.byteStream()?.bufferedReader()?.readText()
            httpResponse.close()
            throw IOException("Unexpected code $httpResponse, recieved: $json")
        }

        val json = httpResponse.body?.byteStream()?.bufferedReader() ?: throw IOException("Response body is null")
        val str = json.readText()
        return objectMapper.readValue(str, ChatResponse::class.java)
    }

    override fun streamChatCompletion(request: ChatRequest): Iterable<ChatResponseChunk> {
        request.stream = true // Set streaming to true
        val httpRequest = buildRequest(request, CHAT_ENDPOINT)

        return object : Iterable<ChatResponseChunk> {
            override fun iterator(): Iterator<ChatResponseChunk> {
                val httpResponse = client.newCall(httpRequest).execute()

                if (!httpResponse.isSuccessful) {
                    httpResponse.close()
                    throw IOException("Unexpected code $httpResponse")
                }

                val reader = httpResponse.body?.byteStream()?.bufferedReader() ?: throw IOException("Response body is null")

                // Only instantiate 1 ChatResponseChunk, otherwise simply update
                // the existing one. This lets us accumulate the message.
                var chunk: ChatResponseChunk? = null

                return object : Iterator<ChatResponseChunk> {
                    private var nextLine: String? = readNextLine(reader)

                    private fun readNextLine(reader: BufferedReader): String? {
                        var line: String?
                        do {
                            line = reader.readLine()
                            if (line == "data: [DONE]") {
                                reader.close()
                                return null
                            }

                            // Check if the line starts with 'data:' and skip empty lines
                        } while (line != null && (line.isEmpty() || !line.startsWith("data: ")))
                        return line?.removePrefix("data: ")
                    }

                    override fun hasNext(): Boolean {
                        return nextLine != null
                    }

                    override fun next(): ChatResponseChunk {
                        val currentLine = nextLine ?: throw NoSuchElementException("No more lines")
                        //println("    $currentLine")
                        chunk = chunk?.apply { update(objectMapper.readTree(currentLine) as ObjectNode) } ?: objectMapper.readValue(currentLine, ChatResponseChunk::class.java)
                        nextLine = readNextLine(reader) // Prepare the next line
                        return chunk!!
                        //return ChatResponseChunk("1", 1, listOf())
                    }
                }
            }
        }
    }

    companion object {
        const val COMPLETIONS_ENDPOINT = "v1/completions"
        const val CHAT_ENDPOINT = "v1/chat/completions"
        const val IMAGE_CREATE_ENDPOINT = "v1/images/generations"
        const val IMAGE_EDIT_ENDPOINT = "v1/images/edits"
        const val IMAGE_VARIATION_ENDPOINT = "v1/images/variations"
    }
}