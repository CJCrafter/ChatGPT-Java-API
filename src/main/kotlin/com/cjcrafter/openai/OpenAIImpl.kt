package com.cjcrafter.openai

import com.cjcrafter.openai.chat.*
import com.cjcrafter.openai.completions.CompletionRequest
import com.cjcrafter.openai.completions.CompletionResponse
import com.cjcrafter.openai.completions.CompletionResponseChunk
import com.cjcrafter.openai.embeddings.EmbeddingsRequest
import com.cjcrafter.openai.embeddings.EmbeddingsResponse
import com.cjcrafter.openai.files.*
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.node.ObjectNode
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.intellij.lang.annotations.Language
import org.jetbrains.annotations.ApiStatus
import java.io.BufferedReader
import java.io.File
import java.io.IOException

open class OpenAIImpl @ApiStatus.Internal constructor(
    protected val apiKey: String,
    protected val organization: String? = null,
    protected val client: OkHttpClient = OkHttpClient(),
    protected val baseUrl: String = "https://api.openai.com",
): OpenAI {
    protected val mediaType = "application/json; charset=utf-8".toMediaType()
    protected val objectMapper = OpenAI.createObjectMapper()

    protected open fun buildRequest(request: Any, endpoint: String): Request {
        val json = objectMapper.writeValueAsString(request)
        val body: RequestBody = json.toRequestBody(mediaType)
        return Request.Builder()
            .url("$baseUrl/$endpoint")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .apply { if (organization != null) addHeader("OpenAI-Organization", organization) }
            .post(body).build()
    }

    protected open fun buildRequestNoBody(endpoint: String, params: Map<String, Any>? = null): Request.Builder{
        val url = "$baseUrl/$endpoint".toHttpUrl().newBuilder()
            .apply {
                params?.forEach { (key, value) -> addQueryParameter(key, value.toString()) }
            }.build().toString()

        return Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $apiKey")
            .apply { if (organization != null) addHeader("OpenAI-Organization", organization) }
    }

    protected open fun buildMultipartRequest(
        endpoint: String,
        function: MultipartBody.Builder.() -> Unit,
    ): Request {

        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .apply(function)
            .build()

        return Request.Builder()
            .url("$baseUrl/$endpoint")
            .addHeader("Authorization", "Bearer $apiKey")
            .apply { if (organization != null) addHeader("OpenAI-Organization", organization) }
            .post(multipartBody).build()
    }

    protected open fun executeRequest(httpRequest: Request): String {
        val httpResponse = client.newCall(httpRequest).execute()
        if (!httpResponse.isSuccessful) {
            val json = httpResponse.body?.byteStream()?.bufferedReader()?.readText()
            httpResponse.close()
            throw IOException("Unexpected code $httpResponse, received: $json")
        }

        val jsonReader = httpResponse.body?.byteStream()?.bufferedReader()
            ?: throw IOException("Response body is null")
        val responseStr = jsonReader.readText()
        OpenAI.logger.debug(responseStr)
        return responseStr
    }

    protected open fun <T> executeRequest(httpRequest: Request, responseType: Class<T>): T {
        val str = executeRequest(httpRequest)
        return objectMapper.readValue(str, responseType)
    }

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
        val httpRequest = buildRequest(request, COMPLETIONS_ENDPOINT)
        return executeRequest(httpRequest, CompletionResponse::class.java)
    }

    override fun streamCompletion(request: CompletionRequest): Iterable<CompletionResponseChunk> {
        @Suppress("DEPRECATION")
        request.stream = true
        val httpRequest = buildRequest(request, COMPLETIONS_ENDPOINT)
        return streamResponses(httpRequest, objectMapper.typeFactory.constructType(CompletionResponseChunk::class.java)) { response, newLine ->
            // We don't have any update logic, so we should ignore the old response and just return a new one
            objectMapper.readValue(newLine, CompletionResponseChunk::class.java)
        }
    }

    override fun createChatCompletion(request: ChatRequest): ChatResponse {
        @Suppress("DEPRECATION")
        request.stream = false // use streamChatCompletion for stream=true
        val httpRequest = buildRequest(request, CHAT_ENDPOINT)
        return executeRequest(httpRequest, ChatResponse::class.java)
    }

    override fun streamChatCompletion(request: ChatRequest): Iterable<ChatResponseChunk> {
        @Suppress("DEPRECATION")
        request.stream = true
        val httpRequest = buildRequest(request, CHAT_ENDPOINT)
        return streamResponses(httpRequest, objectMapper.typeFactory.constructType(ChatResponseChunk::class.java)) { response, newLine ->
            response.update(objectMapper.readTree(newLine) as ObjectNode)
            response
        }
    }

    override fun createEmbeddings(request: EmbeddingsRequest): EmbeddingsResponse {
        val httpRequest = buildRequest(request, EMBEDDINGS_ENDPOINT)
        return executeRequest(httpRequest, EmbeddingsResponse::class.java)
    }

    override fun listFiles(request: ListFilesRequest): ListFilesResponse {
        val httpRequest = buildRequestNoBody(FILES_ENDPOINT, request.toMap()).get().build()
        return executeRequest(httpRequest, ListFilesResponse::class.java)
    }

    override fun uploadFile(request: FileUploadRequest): FileObject {
        val httpRequest = buildMultipartRequest(FILES_ENDPOINT) {
            addFormDataPart("purpose", OpenAI.createObjectMapper().writeValueAsString(request.purpose).trim('"'))
            addFormDataPart("file", request.fileName, request.requestBody)
        }
        return executeRequest(httpRequest, FileObject::class.java)
    }

    override fun deleteFile(fileId: String): FileDeletionStatus {
        val httpRequest = buildRequestNoBody("$FILES_ENDPOINT/$fileId").delete().build()
        return executeRequest(httpRequest, FileDeletionStatus::class.java)
    }

    override fun retrieveFile(fileId: String): FileObject {
        val httpRequest = buildRequestNoBody("$FILES_ENDPOINT/$fileId").get().build()
        return executeRequest(httpRequest, FileObject::class.java)
    }

    override fun retrieveFileContents(fileId: String): String {
        val httpRequest = buildRequestNoBody("$FILES_ENDPOINT/$fileId/content").get().build()
        return executeRequest(httpRequest)
    }


    companion object {
        const val COMPLETIONS_ENDPOINT = "v1/completions"
        const val CHAT_ENDPOINT = "v1/chat/completions"
        const val EMBEDDINGS_ENDPOINT = "v1/embeddings"
        const val FILES_ENDPOINT = "v1/files"
    }
}