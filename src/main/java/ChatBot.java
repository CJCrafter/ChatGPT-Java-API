import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
     * {@link ChatCompletionResponse#getChoices()}.
     *
     * @param request The input information for ChatGPT.
     * @return The returned response.
     * @throws IOException              If an IO Exception occurs.
     * @throws IllegalArgumentException If the input arguments are invalid.
     */
    public ChatCompletionResponse generateResponse(ChatCompletionRequest request) throws IOException {
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

            return new ChatCompletionResponse(rootObject);
        } catch (Throwable ex) {
            System.err.println("Some error occurred whilst using the Chat Completion API");
            System.err.println("Request:\n\n" + json);
            System.err.println("\nRoot Object:\n\n" + rootObject);
            throw ex;
        }
    }


    /**
     * The ChatGPT API takes a list of 'roles' and 'content'. The role is
     * one of 3 options: system, assistant, and user. 'System' is used to
     * prompt ChatGPT before the user gives input. 'Assistant' is a message
     * from ChatGPT. 'User' is a message from the human.
     */
    public static final class ChatMessage {

        private final String role;
        private final String content;

        /**
         * Constructor requires who sent the message, and the content of the
         * message.
         *
         * @param role    Who sent the message.
         * @param content The raw content of the message.
         */
        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public ChatMessage(JsonObject json) {
            this(json.get("role").getAsString(), json.get("content").getAsString());
        }

        public String getRole() {
            return this.role;
        }

        public String getContent() {
            return this.content;
        }
    }

    /**
     * These are the arguments that control the result of the output. For more
     * information, refer to the <a href="https://platform.openai.com/docs/api-reference/completions/create">OpenAI Docs</a>.
     */
    public static final class ChatCompletionRequest {

        private final String model;
        private final List<ChatMessage> messages;
        private final float temperature;
        @SerializedName("top_p")
        private final float topP;
        private final int n;
        private final boolean stream;
        private final String stop;
        @SerializedName("max_tokens")
        private final Integer maxTokens;
        @SerializedName("presence_penalty")
        private final float presencePenalty;
        @SerializedName("frequency_penalty")
        private final float frequencyPenalty;
        @SerializedName("logit_bias")
        private final JsonObject logitBias;
        private final String user;

        /**
         * Shorthand constructor for {@link #ChatCompletionRequest(String, List, float, float, int, boolean, String, Integer, float, float, JsonObject, String)}
         *
         * @param model    The model to use to generate the text. Recommended: "gpt-3.5-turbo"
         * @param messages All previous messages from the conversation.
         */
        public ChatCompletionRequest(String model, List<ChatMessage> messages) {
            this(model, messages, 1.0f, 1.0f, 1, false, null, null, 0f, 0f, null, null);
        }

        /**
         * @param model            The model used to generate the text. Recommended: "gpt-3.5-turbo."
         * @param messages         All previous messages from the conversation.
         * @param temperature      How "creative" the results are. [0.0, 2.0].
         * @param topP             Controls how "on topic" the tokens are.
         * @param n                Controls how many responses to generate. Numbers >1 will chew through your tokens.
         * @param stream           <b>UNTESTED</b> recommend keeping this false.
         * @param stop             The sequence used to stop generating tokens.
         * @param maxTokens        The maximum number of tokens to use.
         * @param presencePenalty  Prevent talking about duplicate topics.
         * @param frequencyPenalty Prevent repeating the same text.
         * @param logitBias        Control specific tokens from being used.
         * @param user             Who send this request (for moderation).
         */
        public ChatCompletionRequest(String model, List<ChatMessage> messages, float temperature, float topP, int n, boolean stream, String stop, Integer maxTokens, float presencePenalty, float frequencyPenalty, JsonObject logitBias, String user) {
            this.model = model;
            this.messages = new ArrayList<>(messages); // Use a mutable list
            this.temperature = temperature;
            this.topP = topP;
            this.n = n;
            this.stream = stream;
            this.stop = stop;
            this.maxTokens = maxTokens;
            this.presencePenalty = presencePenalty;
            this.frequencyPenalty = frequencyPenalty;
            this.logitBias = logitBias;
            this.user = user;
        }

        public String getModel() {
            return this.model;
        }

        public List<ChatMessage> getMessages() {
            return this.messages;
        }

        public float getTemperature() {
            return this.temperature;
        }

        public float getTopP() {
            return this.topP;
        }

        public int getN() {
            return this.n;
        }

        public boolean getStream() {
            return this.stream;
        }

        public String getStop() {
            return this.stop;
        }

        public Integer getMaxTokens() {
            return this.maxTokens;
        }

        public float getPresencePenalty() {
            return this.presencePenalty;
        }

        public float getFrequencyPenalty() {
            return this.frequencyPenalty;
        }

        public JsonObject getLogitBias() {
            return this.logitBias;
        }

        public String getUser() {
            return this.user;
        }
    }

    /**
     * This is the object returned from the API. You want to access choices[0]
     * to get your response.
     */
    public static final class ChatCompletionResponse {

        private final String id;
        private final String object;
        private final long created;
        private final List<ChatCompletionChoice> choices;
        private final ChatCompletionUsage usage;

        public ChatCompletionResponse(String id, String object, long created, List<ChatCompletionChoice> choices, ChatCompletionUsage usage) {
            super();
            this.id = id;
            this.object = object;
            this.created = created;
            this.choices = choices;
            this.usage = usage;
        }

        public ChatCompletionResponse(JsonObject json) {
            this(json.get("id").getAsString(), json.get("object").getAsString(), json.get("created").getAsLong(), StreamSupport.stream(json.get("choices").getAsJsonArray().spliterator(), false).map(element -> new ChatCompletionChoice(element.getAsJsonObject())).collect(Collectors.toList()), new ChatCompletionUsage(json.get("usage").getAsJsonObject()));
        }

        public String getId() {
            return this.id;
        }

        public String getObject() {
            return this.object;
        }

        public long getCreated() {
            return this.created;
        }

        public List<ChatCompletionChoice> getChoices() {
            return this.choices;
        }

        public ChatCompletionUsage getUsage() {
            return this.usage;
        }
    }

    public static final class ChatCompletionChoice {

        private final int index;
        private final ChatMessage message;
        private final String finishReason;

        /**
         * Holds the data for 1 generated text completion.
         *
         * @param index The index in the array... 0 if n=1.
         * @param message The generated text.
         * @param finishReason Why did the bot stop generating tokens?
         */
        public ChatCompletionChoice(int index, ChatMessage message, String finishReason) {
            super();
            this.index = index;
            this.message = message;
            this.finishReason = finishReason;
        }

        public ChatCompletionChoice(JsonObject json) {
            this(json.get("index").getAsInt(), new ChatMessage(json.get("message").getAsJsonObject()), json.get("finish_reason").toString());
        }

        public int getIndex() {
            return this.index;
        }

        public ChatMessage getMessage() {
            return this.message;
        }

        public String getFinishReason() {
            return this.finishReason;
        }
    }

    public static final class ChatCompletionUsage {
        private final int promptTokens;
        private final int completionTokens;
        private final int totalTokens;

        /**
         * Holds how many tokens that were used by your API request. Use these
         * tokens to calculate how much money you have spent on each request.
         *
         * @param promptTokens How many tokens the input used.
         * @param completionTokens How many tokens the output used.
         * @param totalTokens How many tokens in total.
         */
        public ChatCompletionUsage(int promptTokens, int completionTokens, int totalTokens) {
            this.promptTokens = promptTokens;
            this.completionTokens = completionTokens;
            this.totalTokens = totalTokens;
        }

        public ChatCompletionUsage(JsonObject json) {
            this(json.get("prompt_tokens").getAsInt(), json.get("completion_tokens").getAsInt(), json.get("total_tokens").getAsInt());
        }

        public int getPromptTokens() {
            return this.promptTokens;
        }

        public int getCompletionTokens() {
            return this.completionTokens;
        }

        public int getTotalTokens() {
            return this.totalTokens;
        }
    }
}
