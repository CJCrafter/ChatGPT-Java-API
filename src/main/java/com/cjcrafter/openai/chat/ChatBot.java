package com.cjcrafter.openai.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * The ChatBot class wraps the OpenAI API and lets you send messages and
 * receive responses. For more information on how this works, check out
 * the <a href="https://platform.openai.com/docs/api-reference/completions">OpenAI Documentation</a>).
 */
public class ChatBot {

    private final OkHttpClient client;
    private final MediaType mediaType;
    private final Gson gson;
    private final String apiKey;

    /**
     * Constructor requires your private API key.
     *
     * @param apiKey Your OpenAI API key that starts with "sk-".
     */
    public ChatBot(String apiKey) {
        this.apiKey = apiKey;
        this.client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();
        this.mediaType = MediaType.get("application/json; charset=utf-8");
        this.gson = (new GsonBuilder()).create();
    }

    /**
     * Blocks the current thread until OpenAI responds to https request. The
     * returned value includes information including tokens, generated text,
     * and stop reason. You can access the generated message through
     * {@link ChatResponse#getChoices()}.
     *
     * @param request The input information for ChatGPT.
     * @return The returned response.
     * @throws IOException              If an IO Exception occurs.
     * @throws IllegalArgumentException If the input arguments are invalid.
     */
    public ChatResponse generateResponse(ChatRequest request) throws IOException {
        String json = this.gson.toJson(request);
        RequestBody body = RequestBody.create(json, this.mediaType);
        Request httpRequest = (new Request.Builder()).url("https://api.openai.com/v1/chat/completions")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + this.apiKey)
                .post(body).build();

        // Save the JsonObject to check for errors
        JsonObject rootObject = null;
        try (Response response = this.client.newCall(httpRequest).execute()) {

            // Servers respond to API calls with json blocks. Since raw JSON isn't
            // very developer friendly, we wrap for easy data access.
            rootObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
            if (rootObject.has("error"))
                throw new IllegalArgumentException(rootObject.get("error").getAsJsonObject().get("message").getAsString());

            return new ChatResponse(rootObject);
        } catch (Throwable ex) {
            System.err.println("Some error occurred whilst using the Chat Completion API");
            System.err.println("Request:\n\n" + json);
            System.err.println("\nRoot Object:\n\n" + rootObject);
            throw ex;
        }
    }
}
