package com.cjcrafter.openai.chat

import com.google.gson.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * The ChatBot class wraps the OpenAI API and lets you send messages and
 * receive responses. For more information on how this works, check out
 * the [OpenAI Documentation](https://platform.openai.com/docs/api-reference/chat)).
 *
 * [com.cjcrafter.openai.chat] differs from [com.cjcrafter.openai.completions].
 * Chat has conversational memory, and is best suited to generate human-readable
 * text. Think of chat as a human that insists on giving elaborate responses.
 * If you are looking for more robotic or specific responses, consider using
 * completions instead.
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
 * @property apiKey Your OpenAI API key. It starts with `"sk-"` (without the quotes).
 * @constructor Create a ChatBot for responding to requests.
 */
class ChatBot(private val apiKey: String) {

    private val client: OkHttpClient = Builder()
        .connectTimeout(0, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.SECONDS).build()
    private val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(ChatUser::class.java, JsonSerializer<ChatUser> { src, _, context -> context!!.serialize(src!!.name.lowercase())!! })
        .create()

    /**
     * Blocks the current thread until OpenAI responds to https request. The
     * returned value includes information including tokens, generated text,
     * and stop reason. You can access the generated message through
     * [ChatResponse.choices].
     *
     * @param request The input information for ChatGPT.
     * @return The returned response.
     * @throws IOException              If an IO Exception occurs.
     * @throws IllegalArgumentException If the input arguments are invalid.
     */
    @Throws(IOException::class)
    fun generateResponse(request: ChatRequest?): ChatResponse {
        val json = gson.toJson(request)
        val body: RequestBody = json.toRequestBody(mediaType)
        val httpRequest: Request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(body).build()

        // Save the JsonObject to check for errors
        var rootObject: JsonObject? = null
        try {
            client.newCall(httpRequest).execute().use { response ->

                // Servers respond to API calls with json blocks. Since raw JSON isn't
                // very developer friendly, we wrap for easy data access.
                rootObject = JsonParser.parseString(response.body!!.string()).asJsonObject
                require(!rootObject!!.has("error")) { rootObject!!.get("error").asJsonObject["message"].asString }
                return ChatResponse(rootObject!!)
            }
        } catch (ex: Throwable) {
            System.err.println("Some error occurred whilst using the Chat Completion API")
            System.err.println("Request:\n\n$json")
            System.err.println("\nRoot Object:\n\n$rootObject")
            throw ex
        }
    }
}