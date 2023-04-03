package com.cjcrafter.openai

import com.cjcrafter.openai.gson.ChatChoiceChunkAdapter
import com.cjcrafter.openai.chat.*
import com.cjcrafter.openai.completions.CompletionRequest
import com.cjcrafter.openai.completions.CompletionResponse
import com.cjcrafter.openai.completions.CompletionResponseChunk
import com.cjcrafter.openai.exception.OpenAIError
import com.cjcrafter.openai.exception.WrappedIOError
import com.cjcrafter.openai.gson.ChatUserAdapter
import com.cjcrafter.openai.gson.FinishReasonAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.lang.IllegalStateException
import java.util.ArrayList
import java.util.function.Consumer

/**
 * The `OpenAI` class contains all the API calls to OpenAI's endpoint. Whether
 * you are working with images, chat, or completions, you need to have an
 * `OpenAI` instance to make the API requests.
 *
 * To get your API key:
 * 1. Log in to your account: Go to [https://www.openai.com/](openai.com) and
 * log in.
 * 2. Access the API dashboard: After logging in, click on the "API" tab.
 * 3. Choose a subscription plan: Select a suitable plan based on your needs
 * and complete the payment process.
 * 4. Obtain your API key: After subscribing to a plan, you will be redirected
 * to the API dashboard, where you can find your unique API key. Copy and store it securely.
 *
 * All API methods in this class have a non-blocking option which will enqueues
 * the HTTPS request on a different thread. These method names have `Async
 * appended to the end of their names.
 *
 * Completions API:
 * * [createCompletion]
 * * [streamCompletion]
 * * [createCompletionAsync]
 * * [streamCompletionAsync]
 *
 * Chat API:
 * * [createChatCompletion]
 * * [streamChatCompletion]
 * * [createChatCompletionAsync]
 * * [streamChatCompletionAsync]
 *
 * @property apiKey Your OpenAI API key. It starts with `"sk-"` (without the quotes).
 * @property organization If you belong to multiple organizations, specify which one to use (else `null`).
 * @property client Controls proxies, timeouts, etc.
 * @constructor Create a ChatBot for responding to requests.
 */
class OpenAI @JvmOverloads constructor(
    private val apiKey: String,
    private val organization: String? = null,
    private val client: OkHttpClient = OkHttpClient()
) {
    private val mediaType = "application/json; charset=utf-8".toMediaType()
    private val gson = createGson()

    private fun buildRequest(request: Any, endpoint: String): Request {
        val json = gson.toJson(request)
        val body: RequestBody = json.toRequestBody(mediaType)
        return Request.Builder()
            .url("https://api.openai.com/v1/$endpoint")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .apply { if (organization != null) addHeader("OpenAI-Organization", organization) }
            .post(body).build()
    }

    /**
     * Predicts which text comes after the prompt, thus "completing" the text.
     *
     * Calls OpenAI's Completions API and waits until the entire completion is
     * generated. When [CompletionRequest.maxTokens] is a big number, it will
     * take a long time to generate all the tokens, so it is recommended to use
     * [streamCompletionAsync] instead to allow users to see partial completions.
     *
     * This method blocks the current thread until the stream is complete. For
     * non-blocking options, use [streamCompletionAsync]. It is important to
     * consider which thread you are currently running on. Running this method
     * on [javax.swing]'s thread, for example, will cause your UI to freeze
     * temporarily.
     *
     * @param request The data to send to the API endpoint.
     * @since 1.3.0
     */
    @Throws(OpenAIError::class)
    fun createCompletion(request: CompletionRequest): CompletionResponse {
        @Suppress("DEPRECATION")
        request.stream = false // use streamCompletion for stream=true
        val httpRequest = buildRequest(request, COMPLETIONS_ENDPOINT)

        try {
            val httpResponse = client.newCall(httpRequest).execute();
            lateinit var response: CompletionResponse
            MyCallback(true, { throw it }) {
                response = gson.fromJson(it, CompletionResponse::class.java)
            }.onResponse(httpResponse)

            return response
        } catch (ex: IOException) {
            throw WrappedIOError(ex)
        }
    }

    /**
     * Predicts which text comes after the prompt, thus "completing" the text.
     *
     * Calls OpenAI's Completions API and waits until the entire completion is
     * generated. When [CompletionRequest.maxTokens] is a big number, it will
     * take a long time to generate all the tokens, so it is recommended to use
     * [streamCompletionAsync] instead to allow users to see partial completions.
     *
     * This method will not block the current thread. The code block [onResponse]
     * will be run later on a different thread. Due to the different thread, it
     * is important to consider thread safety in the context of your program. To
     * avoid thread safety issues, use [streamCompletion] to block the main thread.
     *
     * @param request The data to send to the API endpoint.
     * @param onResponse The code to execute for every chunk of text.
     * @param onFailure The code to execute when a failure occurs.
     * @since 1.3.0
     */
    @JvmOverloads
    fun createCompletionAsync(
        request: CompletionRequest,
        onResponse: Consumer<CompletionResponse>,
        onFailure: Consumer<OpenAIError> = Consumer { it.printStackTrace() }
    ) {
        @Suppress("DEPRECATION")
        request.stream = false // use streamCompletionAsync for stream=true
        val httpRequest = buildRequest(request, COMPLETIONS_ENDPOINT)

        client.newCall(httpRequest).enqueue(MyCallback(false, onFailure) {
            val response = gson.fromJson(it, CompletionResponse::class.java)
            onResponse.accept(response)
        })
    }

    /**
     * Predicts which text comes after the prompt, thus "completing" the text.
     *
     * Calls OpenAI's Completions API using a *stream* of data. Streams allow
     * developers to access tokens in real time as they are generated. This is
     * used to create the "scrolling text" or "living typing" effect. Using
     * `streamCompletion` gives users information immediately, as opposed to
     * `createCompletion` where you have to wait for the entire message to
     * generate.
     *
     * This method blocks the current thread until the stream is complete. For
     * non-blocking options, use [streamCompletionAsync]. It is important to
     * consider which thread you are currently running on. Running this method
     * on [javax.swing]'s thread, for example, will cause your UI to freeze
     * temporarily.
     *
     * @param request The data to send to the API endpoint.
     * @param onResponse The code to execute for every chunk of text.
     * @param onFailure The code to execute when a failure occurs.
     * @since 1.3.0
     */
    @JvmOverloads
    fun streamCompletion(
        request: CompletionRequest,
        onResponse: Consumer<CompletionResponseChunk>,
        onFailure: Consumer<OpenAIError> = Consumer { it.printStackTrace() }
    ) {
        @Suppress("DEPRECATION")
        request.stream = true // use createCompletion for stream=false
        val httpRequest = buildRequest(request, COMPLETIONS_ENDPOINT)

        try {
            val httpResponse = client.newCall(httpRequest).execute()
            MyCallback(true, onFailure) {
                val response = gson.fromJson(it, CompletionResponseChunk::class.java)
                onResponse.accept(response)
            }.onResponse(httpResponse)
        } catch (ex: IOException) {
            onFailure.accept(WrappedIOError(ex))
        }
    }

    /**
     * Predicts which text comes after the prompt, thus "completing" the text.
     *
     * Calls OpenAI's Completions API using a *stream* of data. Streams allow
     * developers to access tokens in real time as they are generated. This is
     * used to create the "scrolling text" or "living typing" effect. Using
     * `streamCompletionAsync` gives users information immediately, as opposed to
     * `createCompletionAsync` where you have to wait for the entire message to
     * generate.
     *
     * This method will not block the current thread. The code block [onResponse]
     * will be run later on a different thread. Due to the different thread, it
     * is important to consider thread safety in the context of your program. To
     * avoid thread safety issues, use [streamCompletion] to block the main thread.
     *
     * @param request The data to send to the API endpoint.
     * @param onResponse The code to execute for every chunk of text.
     * @param onFailure The code to execute when a failure occurs.
     * @since 1.3.0
     */
    @JvmOverloads
    fun streamCompletionAsync(
        request: CompletionRequest,
        onResponse: Consumer<CompletionResponseChunk>,
        onFailure: Consumer<OpenAIError> = Consumer { it.printStackTrace() }
    ) {
        @Suppress("DEPRECATION")
        request.stream = true // use createCompletionAsync for stream=false
        val httpRequest = buildRequest(request, COMPLETIONS_ENDPOINT)

        client.newCall(httpRequest).enqueue(MyCallback(true, onFailure) {
            val response = gson.fromJson(it, CompletionResponseChunk::class.java)
            onResponse.accept(response)
        })
    }

    /**
     * Responds to the input in a conversational manner. Chat can "remember"
     * older parts of the conversation by looking at the different messages in
     * the list.
     *
     * Calls OpenAI's Completions API and waits until the entire message is
     * generated. Since generating an entire CHAT message can be time-consuming,
     * it is preferred to use [streamChatCompletionAsync] instead.
     *
     * This method blocks the current thread until the stream is complete. For
     * non-blocking options, use [createChatCompletionAsync]. It is important to
     * consider which thread you are currently running on. Running this method
     * on [javax.swing]'s thread, for example, will cause your UI to freeze
     * temporarily.
     *
     * @param request The data to send to the API endpoint.
     * @return The generated response.
     * @since 1.3.0
     */
    @Throws(OpenAIError::class)
    fun createChatCompletion(request: ChatRequest): ChatResponse {
        @Suppress("DEPRECATION")
        request.stream = false // use streamChatCompletion for stream=true
        val httpRequest = buildRequest(request, CHAT_ENDPOINT)

        try {
            val httpResponse = client.newCall(httpRequest).execute()
            lateinit var response: ChatResponse
            MyCallback(true, { throw it }) {
                response = gson.fromJson(it, ChatResponse::class.java)
            }.onResponse(httpResponse)

            return response
        } catch (ex: IOException) {
            throw WrappedIOError(ex)
        }
    }

    /**
     * Responds to the input in a conversational manner. Chat can "remember"
     * older parts of the conversation by looking at the different messages in
     * the list.
     *
     * Calls OpenAI's Completions API and waits until the entire message is
     * generated. Since generating an entire CHAT message can be time-consuming,
     * it is preferred to use [streamChatCompletionAsync] instead.
     *
     * This method will not block the current thread. The code block [onResponse]
     * will be run later on a different thread. Due to the different thread, it
     * is important to consider thread safety in the context of your program. To
     * avoid thread safety issues, use [streamChatCompletion] to block the main thread.
     *
     * @param request The data to send to the API endpoint.
     * @param onResponse The code to execute for every chunk of text.
     * @param onFailure The code to execute when a failure occurs.
     * @since 1.3.0
     */
    @JvmOverloads
    fun createChatCompletionAsync(
        request: ChatRequest,
        onResponse: Consumer<ChatResponse>,
        onFailure: Consumer<OpenAIError> = Consumer { it.printStackTrace() }
    ) {
        @Suppress("DEPRECATION")
        request.stream = false // use streamChatCompletionAsync for stream=true
        val httpRequest = buildRequest(request, CHAT_ENDPOINT)

        client.newCall(httpRequest).enqueue(MyCallback(false, onFailure) {
            val response = gson.fromJson(it, ChatResponse::class.java)
            onResponse.accept(response)
        })
    }

    /**
     * Responds to the input in a conversational manner. Chat can "remember"
     * older parts of the conversation by looking at the different messages in
     * the list.
     *
     * Calls OpenAI's Completions API using a *stream* of data. Streams allow
     * developers to access tokens in real time as they are generated. This is
     * used to create the "scrolling text" or "living typing" effect. Using
     * `streamCompletion` gives users information immediately, as opposed to
     * `createCompletion` where you have to wait for the entire message to
     * generate.
     *
     * This method blocks the current thread until the stream is complete. For
     * non-blocking options, use [streamCompletionAsync]. It is important to
     * consider which thread you are currently running on. Running this method
     * on [javax.swing]'s thread, for example, will cause your UI to freeze
     * temporarily.
     *
     * @param request The data to send to the API endpoint.
     * @param onResponse The code to execute for every chunk of text.
     * @param onFailure The code to execute when a failure occurs.
     * @since 1.3.0
     */
    @JvmOverloads
    fun streamChatCompletion(
        request: ChatRequest,
        onResponse: Consumer<ChatResponseChunk>,
        onFailure: Consumer<OpenAIError> = Consumer { it.printStackTrace() }
    ) {
        @Suppress("DEPRECATION")
        request.stream = true // use requestResponse for stream=false
        val httpRequest = buildRequest(request, CHAT_ENDPOINT)

        try {
            val httpResponse = client.newCall(httpRequest).execute()
            var response: ChatResponseChunk? = null
            MyCallback(true, onFailure) {
                if (response == null)
                    response = gson.fromJson(it, ChatResponseChunk::class.java)
                else
                    response!!.update(it)

                onResponse.accept(response!!)
            }.onResponse(httpResponse)
        } catch (ex: IOException) {
            onFailure.accept(WrappedIOError(ex))
        }
    }

    /**
     * Responds to the input in a conversational manner. Chat can "remember"
     * older parts of the conversation by looking at the different messages in
     * the list.
     *
     * Calls OpenAI's Completions API using a *stream* of data. Streams allow
     * developers to access tokens in real time as they are generated. This is
     * used to create the "scrolling text" or "live typing" effect. Using
     * `streamChatCompletionAsync` gives users information immediately, as opposed to
     * [createChatCompletionAsync] where you have to wait for the entire message to
     * generate.
     *
     * This method will not block the current thread. The code block [onResponse]
     * will be run later on a different thread. Due to the different thread, it
     * is important to consider thread safety in the context of your program. To
     * avoid thread safety issues, use [streamChatCompletion] to block the main thread.
     *
     * @param request The data to send to the API endpoint.
     * @param onResponse The code to execute for every chunk of text.
     * @param onFailure The code to execute when a failure occurs.
     * @since 1.3.0
     */
    @JvmOverloads
    fun streamChatCompletionAsync(
        request: ChatRequest,
        onResponse: Consumer<ChatResponseChunk>,
        onFailure: Consumer<OpenAIError> = Consumer { it.printStackTrace() }
    ) {
        @Suppress("DEPRECATION")
        request.stream = true // use requestResponse for stream=false
        val httpRequest = buildRequest(request, CHAT_ENDPOINT)

        var response: ChatResponseChunk? = null
        client.newCall(httpRequest).enqueue(MyCallback(true, onFailure) {
            if (response == null)
                response = gson.fromJson(it, ChatResponseChunk::class.java)
            else
                response!!.update(it)

            onResponse.accept(response!!)
        })
    }

    companion object {

        const val COMPLETIONS_ENDPOINT = "completions"
        const val CHAT_ENDPOINT = "chat/completions"

        /**
         * Returns a `Gson` object that can be used to read/write .json files.
         * This can be used to save requests/responses to a file, so you can
         * keep a history of all API calls you've made.
         *
         * This is especially important for [ChatRequest], since users will
         * expect you to save their conversations to be continued at later
         * times.
         *
         * If you want to add your own type adapters, use [createGsonBuilder]
         * instead.
         *
         * @return Google gson serializer for json files.
         */
        @JvmStatic
        fun createGson(): Gson {
            return createGsonBuilder().create()
        }

        /**
         * Returns a `GsonBuilder` with all [com.google.gson.TypeAdapter] used
         * by `com.cjcrafter.openai`. Unless you want to register your own
         * adapters, I recommend using [createGson] instead of this method.
         *
         * @return Google gson builder for serializing json files.
         */
        @JvmStatic
        fun createGsonBuilder(): GsonBuilder {
            return GsonBuilder()
                .registerTypeAdapter(ChatUser::class.java, ChatUserAdapter())
                .registerTypeAdapter(FinishReason::class.java, FinishReasonAdapter())
                .registerTypeAdapter(ChatChoiceChunk::class.java, ChatChoiceChunkAdapter())
        }
    }
}