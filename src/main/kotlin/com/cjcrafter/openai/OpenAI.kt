package com.cjcrafter.openai

import com.cjcrafter.openai.chat.*
import com.cjcrafter.openai.chat.tool.ToolChoice
import com.cjcrafter.openai.completions.CompletionRequest
import com.cjcrafter.openai.completions.CompletionResponse
import com.cjcrafter.openai.completions.CompletionResponseChunk
import com.cjcrafter.openai.jackson.ToolChoiceDeserializer
import com.cjcrafter.openai.jackson.ToolChoiceSerializer
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient

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
    fun streamCompletion(request: CompletionRequest): Iterable<CompletionResponseChunk>

    /**
     * Calls the [chat completions](https://platform.openai.com/docs/api-reference/chat)
     * API endpoint. This method is blocking.
     *
     * @param request The request to send to the API
     * @return The response from the API
     */
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
    fun streamChatCompletion(request: ChatRequest): Iterable<ChatResponseChunk>

    open class Builder internal constructor() {
        protected var apiKey: String? = null
        protected var organization: String? = null
        protected var client: OkHttpClient = OkHttpClient()

        fun apiKey(apiKey: String) = apply { this.apiKey = apiKey }
        fun organization(organization: String?) = apply { this.organization = organization }
        fun client(client: OkHttpClient) = apply { this.client = client }

        open fun build(): OpenAI {
            return OpenAIImpl(
                apiKey ?: throw IllegalStateException("apiKey must be defined to use OpenAI"),
                organization,
                client
            )
        }
    }

    class AzureBuilder internal constructor(): Builder() {
        private var azureBaseUrl: String? = null
        private var apiVersion: String? = null
        private var modelName: String? = null

        fun azureBaseUrl(azureBaseUrl: String) = apply { this.azureBaseUrl = azureBaseUrl }
        fun apiVersion(apiVersion: String) = apply { this.apiVersion = apiVersion }
        fun modelName(modelName: String) = apply { this.modelName = modelName }

        override fun build(): OpenAI {
            return AzureOpenAI(
                apiKey ?: throw IllegalStateException("apiKey must be defined to use OpenAI"),
                organization,
                client,
                azureBaseUrl ?: throw IllegalStateException("azureBaseUrl must be defined for azure"),
                apiVersion ?: throw IllegalStateException("apiVersion must be defined for azure"),
                modelName ?: throw IllegalStateException("modelName must be defined for azure")
            )
        }
    }

    companion object {

        /**
         * Instantiates a builder for a default OpenAI instance. For Azure's
         * OpenAI, use [azureBuilder] instead.
         */
        @JvmStatic
        fun builder() = Builder()

        /**
         * Instantiates a builder for an Azure OpenAI.
         */
        @JvmStatic
        fun azureBuilder() = AzureBuilder()

        /**
         * Returns an ObjectMapper instance with the default OpenAI adapters registered.
         * This can be used to save conversations (and other data) to file.
         */
        fun createObjectMapper(): ObjectMapper = jacksonObjectMapper().apply {
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

            // Register modules with custom serializers/deserializers
            val module = SimpleModule().apply {
                addSerializer(ToolChoice::class.java, ToolChoiceSerializer())
                addDeserializer(ToolChoice::class.java, ToolChoiceDeserializer())
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
