package com.cjcrafter.openai

import com.cjcrafter.openai.assistants.AssistantHandler
import com.cjcrafter.openai.assistants.AssistantHandlerImpl
import com.cjcrafter.openai.chat.*
import com.cjcrafter.openai.completions.CompletionRequest
import com.cjcrafter.openai.completions.CompletionResponse
import com.cjcrafter.openai.completions.CompletionResponseChunk
import com.cjcrafter.openai.embeddings.EmbeddingsRequest
import com.cjcrafter.openai.embeddings.EmbeddingsResponse
import com.cjcrafter.openai.files.*
import com.cjcrafter.openai.threads.ThreadHandler
import com.cjcrafter.openai.threads.ThreadHandlerImpl
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.node.ObjectNode
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.jetbrains.annotations.ApiStatus
import java.io.BufferedReader
import java.io.IOException

open class OpenAIImpl @ApiStatus.Internal constructor(
    protected val apiKey: String,
    protected val organization: String? = null,
    protected val client: OkHttpClient = OkHttpClient(),
    protected val baseUrl: String = "https://api.openai.com",
): OpenAI {
    protected val mediaType = "application/json; charset=utf-8".toMediaType()
    protected val objectMapper = OpenAI.createObjectMapper()

    protected var requestHelper = RequestHelper(apiKey, organization, client, baseUrl)

    private fun <T> streamResponses(
        request: Request,
        responseType: JavaType,
        updateResponse: (T, String) -> T
    ): Iterable<T> {
        return object : Iterable<T> {
            override fun iterator(): Iterator<T> {
                val httpResponse = client.newCall(request).execute()

                if (!httpResponse.isSuccessful) {
                    httpResponse.close()
                    throw IOException("Unexpected code $httpResponse")
                }

                val reader = httpResponse.body?.byteStream()?.bufferedReader()
                    ?: throw IOException("Response body is null")

                var currentResponse: T? = null

                return object : Iterator<T> {
                    private var nextLine: String? = readNextLine(reader)

                    private fun readNextLine(reader: BufferedReader): String? {
                        var line: String?
                        do {
                            line = reader.readLine()
                            OpenAI.logger.debug(line)

                            if (line == "data: [DONE]") {
                                reader.close()
                                return null
                            }
                        } while (line != null && (line.isEmpty() || !line.startsWith("data: ")))
                        return line?.removePrefix("data: ")
                    }

                    override fun hasNext(): Boolean {
                        return nextLine != null
                    }

                    override fun next(): T {
                        val line = nextLine ?: throw NoSuchElementException("No more lines")

                        currentResponse = if (currentResponse == null) {
                            objectMapper.readValue(line, responseType)
                        } else {
                            updateResponse(currentResponse!!, line)
                        }
                        nextLine = readNextLine(reader)
                        return currentResponse!!
                    }
                }
            }
        }
    }

    override fun createCompletion(request: CompletionRequest): CompletionResponse {
        @Suppress("DEPRECATION")
        request.stream = false // use streamCompletion for stream=true
        val httpRequest = requestHelper.buildRequest(request, COMPLETIONS_ENDPOINT).build()
        return requestHelper.executeRequest(httpRequest, CompletionResponse::class.java)
    }

    override fun streamCompletion(request: CompletionRequest): Iterable<CompletionResponseChunk> {
        @Suppress("DEPRECATION")
        request.stream = true
        val httpRequest = requestHelper.buildRequest(request, COMPLETIONS_ENDPOINT).build()
        return streamResponses(httpRequest, objectMapper.typeFactory.constructType(CompletionResponseChunk::class.java)) { response, newLine ->
            // We don't have any update logic, so we should ignore the old response and just return a new one
            objectMapper.readValue(newLine, CompletionResponseChunk::class.java)
        }
    }

    override fun createChatCompletion(request: ChatRequest): ChatResponse {
        @Suppress("DEPRECATION")
        request.stream = false // use streamChatCompletion for stream=true
        val httpRequest = requestHelper.buildRequest(request, CHAT_ENDPOINT).build()
        return requestHelper.executeRequest(httpRequest, ChatResponse::class.java)
    }

    override fun streamChatCompletion(request: ChatRequest): Iterable<ChatResponseChunk> {
        @Suppress("DEPRECATION")
        request.stream = true
        val httpRequest = requestHelper.buildRequest(request, CHAT_ENDPOINT).build()
        return streamResponses(httpRequest, objectMapper.typeFactory.constructType(ChatResponseChunk::class.java)) { response, newLine ->
            response.update(objectMapper.readTree(newLine) as ObjectNode)
            response
        }
    }

    override fun createEmbeddings(request: EmbeddingsRequest): EmbeddingsResponse {
        val httpRequest = requestHelper.buildRequest(request, EMBEDDINGS_ENDPOINT).build()
        return requestHelper.executeRequest(httpRequest, EmbeddingsResponse::class.java)
    }

    private var files0: FileHandlerImpl? = null
    override val files: FileHandler
        get() = files0 ?: FileHandlerImpl(requestHelper, FILES_ENDPOINT).also { files0 = it }

    private var assistants0: AssistantHandlerImpl? = null
    override val assistants: AssistantHandler
        get() = assistants0 ?: AssistantHandlerImpl(requestHelper, ASSISTANTS_ENDPOINT).also { assistants0 = it }

    private var threads0: ThreadHandlerImpl? = null
    override val threads: ThreadHandler
        get() = threads0 ?: ThreadHandlerImpl(requestHelper, ASSISTANTS_ENDPOINT).also { threads0 = it }

    companion object {
        const val COMPLETIONS_ENDPOINT = "v1/completions"
        const val CHAT_ENDPOINT = "v1/chat/completions"
        const val EMBEDDINGS_ENDPOINT = "v1/embeddings"
        const val FILES_ENDPOINT = "v1/files"
        const val ASSISTANTS_ENDPOINT = "v1/assistants"
    }
}