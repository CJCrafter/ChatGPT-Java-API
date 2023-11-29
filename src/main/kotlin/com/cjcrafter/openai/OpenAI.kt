package com.cjcrafter.openai

import com.cjcrafter.openai.assistants.AssistantHandler
import com.cjcrafter.openai.assistants.AssistantHandlerImpl
import com.cjcrafter.openai.chat.*
import com.cjcrafter.openai.chat.tool.ToolChoice
import com.cjcrafter.openai.completions.CompletionRequest
import com.cjcrafter.openai.completions.CompletionResponse
import com.cjcrafter.openai.completions.CompletionResponseChunk
import com.cjcrafter.openai.embeddings.EmbeddingsRequest
import com.cjcrafter.openai.embeddings.EmbeddingsResponse
import com.cjcrafter.openai.files.*
import com.cjcrafter.openai.threads.ThreadHandler
import com.cjcrafter.openai.threads.message.TextAnnotation
import com.cjcrafter.openai.util.OpenAIDslMarker
import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import org.jetbrains.annotations.ApiStatus
import org.jetbrains.annotations.Contract
import org.slf4j.LoggerFactory

/**
 * The main interface for the OpenAI API. This interface contains methods for
 * all the API endpoints. To instantiate an instance of this interface, use
 * [builder].
 *
 * All the methods in this class are blocking (except the stream methods,
 * [streamCompletion] and [streamChatCompletion], which return an iterator
 * which blocks the thread).
 *
 * The methods in this class all throw io exceptions if the request fails. The
 * error message will contain the JSON response from the API (if present).
 * Common errors include:
 * 1. Not having a valid API key
 * 2. Passing a bad parameter to a request
 */
interface OpenAI {

    /**
     * Calls the [completions](https://platform.openai.com/docs/api-reference/completions)
     * API endpoint. This method is blocking.
     *
     * Completions are considered Legacy, and OpenAI officially recommends that
     * all developers use the **chat completion** endpoint instead. See
     * [createChatCompletion].
     *
     * @param request The request to send to the API
     * @return The response from the API
     */
    @ApiStatus.Obsolete
    @Contract(pure = true)
    fun createCompletion(request: CompletionRequest): CompletionResponse

    /**
     * Calls the [completions](https://platform.openai.com/docs/api-reference/completions)
     * API endpoint and streams each token 1 at a time for a faster response
     * time.
     *
     * This method is **technically** not blocking, but the returned iterable
     * will block until the next token is generated.
     * ```
     * // Each iteration of the loop will block until the next token is streamed
     * for (chunk in openAI.streamCompletion(request)) {
     *     // Do something with the chunk
     * }
     * ```
     *
     * Completions are considered Legacy, and OpenAI officially recommends that
     * all developers use the **chat completion** endpoint isntead. See
     * [streamChatCompletion].
     *
     * @param request The request to send to the API
     * @return The response from the API
     */
    @ApiStatus.Obsolete
    @Contract(pure = true)
    fun streamCompletion(request: CompletionRequest): Iterable<CompletionResponseChunk>

    /**
     * Calls the [chat completions](https://platform.openai.com/docs/api-reference/chat)
     * API endpoint. This method is blocking.
     *
     * @param request The request to send to the API
     * @return The response from the API
     */
    @Contract(pure = true)
    fun createChatCompletion(request: ChatRequest): ChatResponse

    /**
     * Calls the [chat completions](https://platform.openai.com/docs/api-reference/chat)
     * API endpoint and streams each token 1 at a time for a faster response.
     *
     * This method is **technically** not blocking, but the returned iterable
     * will block until the next token is generated.
     * ```
     * // Each iteration of the loop will block until the next token is streamed
     * for (chunk in openAI.streamChatCompletion(request)) {
     *    // Do something with the chunk
     * }
     * ```
     *
     * @param request The request to send to the API
     * @return The response from the API
     */
    @Contract(pure = true)
    fun streamChatCompletion(request: ChatRequest): Iterable<ChatResponseChunk>

    /**
     * Calls the [embeddings](https://beta.openai.com/docs/api-reference/embeddings)
     * API endpoint to generate the vector representation of text. The returned
     * vector can be used in Machine Learning models. This method is blocking.
     *
     * @param request The request to send to the API
     * @return The response from the API
     */
    @Contract(pure = true)
    @ApiStatus.Experimental
    fun createEmbeddings(request: EmbeddingsRequest): EmbeddingsResponse

    /**
     * Returns the handler for the files endpoint. This handler can be used to
     * create, retrieve, and delete files.
     */
    val files: FileHandler

    /**
     * Returns the handler for the assistants endpoint. This handler can be used
     * to create, retrieve, and delete assistants.
     */
    val assistants: AssistantHandler

    /**
     * Returns the handler for the threads endpoint. This handler can be used
     * to create, retrieve, and delete threads.
     */
    val threads: ThreadHandler

    @OpenAIDslMarker
    open class Builder internal constructor() {
        protected var apiKey: String? = null
        protected var organization: String? = null
        protected var client: OkHttpClient = OkHttpClient()
        protected var baseUrl: String = "https://api.openai.com"

        fun apiKey(apiKey: String) = apply { this.apiKey = apiKey }
        fun organization(organization: String?) = apply { this.organization = organization }
        fun client(client: OkHttpClient) = apply { this.client = client }
        fun baseUrl(baseUrl: String) = apply { this.baseUrl = baseUrl }

        @Contract(pure = true)
        open fun build(): OpenAI {
            return OpenAIImpl(
                apiKey = apiKey ?: throw IllegalStateException("apiKey must be defined to use OpenAI"),
                organization = organization,
                client = client,
                baseUrl = baseUrl,
            )
        }
    }

    @OpenAIDslMarker
    class AzureBuilder internal constructor(): Builder() {
        private var apiVersion: String? = null
        private var modelName: String? = null

        fun apiVersion(apiVersion: String) = apply { this.apiVersion = apiVersion }
        fun modelName(modelName: String) = apply { this.modelName = modelName }

        @Contract(pure = true)
        override fun build(): OpenAI {
            return AzureOpenAI(
                apiKey = apiKey ?: throw IllegalStateException("apiKey must be defined to use OpenAI"),
                organization = organization,
                client = client,
                baseUrl = if (baseUrl == "https://api.openai.com") throw IllegalStateException("baseUrl must be set to an azure endpoint") else baseUrl,
                apiVersion = apiVersion ?: throw IllegalStateException("apiVersion must be defined for azure"),
                modelName = modelName ?: throw IllegalStateException("modelName must be defined for azure")
            )
        }
    }

    companion object {

        internal val logger = LoggerFactory.getLogger(OpenAI::class.java)

        /**
         * Instantiates a builder for a default OpenAI instance. For Azure's
         * OpenAI, use [azureBuilder] instead.
         */
        @JvmStatic
        @Contract(pure = true)
        fun builder() = Builder()

        /**
         * Instantiates a builder for an Azure OpenAI.
         */
        @JvmStatic
        @Contract(pure = true)
        fun azureBuilder() = AzureBuilder()

        /**
         * Returns an ObjectMapper instance with the default OpenAI adapters registered.
         * This can be used to save conversations (and other data) to file.
         */
        @Contract(pure = true)
        fun createObjectMapper(): ObjectMapper = jacksonObjectMapper().apply {
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

            // By default, Jackson can serialize fields AND getters. We just want fields.
            setVisibility(serializationConfig.getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
            )

            // Register modules with custom serializers/deserializers
            val module = SimpleModule().apply {
                addSerializer(ToolChoice::class.java, ToolChoice.serializer())
                addDeserializer(ToolChoice::class.java, ToolChoice.deserializer())
            }

            registerModule(module)
        }

        /**
         * Extension function to stream a completion using kotlin coroutines.
         */
        fun OpenAI.streamCompletion(request: CompletionRequest, consumer: (CompletionResponseChunk) -> Unit) {
            for (chunk in streamCompletion(request))
                consumer(chunk)
        }

        /**
         * Extension function to stream a chat completion using kotlin coroutines.
         */
        fun OpenAI.streamChatCompletion(request: ChatRequest, consumer: (ChatResponseChunk) -> Unit) {
            for (chunk in streamChatCompletion(request))
                consumer(chunk)
        }
    }
}