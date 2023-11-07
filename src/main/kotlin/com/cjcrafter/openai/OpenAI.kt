package com.cjcrafter.openai

import com.cjcrafter.openai.chat.*
import com.cjcrafter.openai.completions.CompletionRequest
import com.cjcrafter.openai.completions.CompletionResponse
import com.cjcrafter.openai.completions.CompletionResponseChunk
import com.cjcrafter.openai.exception.OpenAIError
import com.cjcrafter.openai.gson.ChatChoiceChunkAdapter
import com.cjcrafter.openai.gson.ChatUserAdapter
import com.cjcrafter.openai.gson.FinishReasonAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient

interface OpenAI {

    @Throws(OpenAIError::class)
    fun createCompletion(request: CompletionRequest): CompletionResponse

    @Throws(OpenAIError::class)
    fun streamCompletion(request: CompletionRequest): Iterable<CompletionResponseChunk>

    @Throws(OpenAIError::class)
    fun createChatCompletion(request: ChatRequest): ChatResponse

    @Throws(OpenAIError::class)
    fun streamChatCompletion(request: ChatRequest): Iterable<ChatResponseChunk>

    open class Builder {
        protected var apiKey: String? = null
        protected var organization: String? = null
        protected var client: OkHttpClient = OkHttpClient()

        fun apiKey(apiKey: String) = apply { this.apiKey = apiKey }

        fun organization(organization: String?) = apply { this.organization = organization }

        fun client(client: OkHttpClient) = apply { this.client = client }

        open fun build(): OpenAI {
            checkNotNull(apiKey) { "apiKey must be defined to use OpenAI" }
            return OpenAIImpl(apiKey!!, organization, client)
        }
    }

    class AzureBuilder : Builder() {
        private var azureBaseUrl: String? = null
        private var apiVersion: String? = null
        private var modelName: String? = null

        fun azureBaseUrl(azureBaseUrl: String) = apply { this.azureBaseUrl = azureBaseUrl }

        fun apiVersion(apiVersion: String) = apply { this.apiVersion = apiVersion }

        fun modelName(modelName: String) = apply { this.modelName = modelName }

        override fun build(): OpenAI {
            checkNotNull(apiKey) { "apiKey must be defined to use OpenAI" }
            checkNotNull(azureBaseUrl) { "azureBaseUrl must be defined for azure" }
            checkNotNull(apiVersion) { "apiVersion must be defined for azure" }
            checkNotNull(modelName) { "modelName must be defined for azure" }

            return AzureOpenAI(apiKey!!, organization, client, azureBaseUrl!!, apiVersion!!, modelName!!)
        }
    }

    companion object {

        @JvmStatic
        fun builder() = Builder()

        @JvmStatic
        fun azureBuilder() = AzureBuilder()

        @JvmStatic
        fun createGson(): Gson = createGsonBuilder().create()

        @JvmStatic
        fun createGsonBuilder(): GsonBuilder {
            return GsonBuilder()
                .registerTypeAdapter(ChatUser::class.java, ChatUserAdapter())
                .registerTypeAdapter(FinishReason::class.java, FinishReasonAdapter())
                .registerTypeAdapter(ChatChoiceChunk::class.java, ChatChoiceChunkAdapter())
        }

        fun OpenAI.streamCompletion(request: CompletionRequest, consumer: (CompletionResponseChunk) -> Unit) {
            for (chunk in streamCompletion(request))
                consumer(chunk)
        }

        fun OpenAI.streamChatCompletion(request: ChatRequest, consumer: (ChatResponseChunk) -> Unit) {
            for (chunk in streamChatCompletion(request))
                consumer(chunk)
        }
    }
}
