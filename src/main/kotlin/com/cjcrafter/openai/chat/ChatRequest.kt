package com.cjcrafter.openai.chat

import com.google.gson.annotations.SerializedName

/**
 * [ChatRequest] holds the configurable options that can be sent to the OpenAI
 * Chat API. For most use cases, you only need to set [model] and [messages].
 * For more detailed descriptions for each option, refer to the
 * [Chat Wiki](https://platform.openai.com/docs/api-reference/chat).
 *
 * [messages] stores **ALL** previous messages from the conversation. It is
 * **YOUR RESPONSIBILITY** to store and update this list for your conversations
 * (Check out the [JavaChatTest] or the READEME.md for an example). In general,
 * the list should start with a [ChatUser.SYSTEM] message, then alternate between
 * [ChatUser.USER] and [ChatUser.ASSISTANT]. Failing to follow this order may
 * confuse the model and cause it to apologize for not responding.
 *
 * It is best practice to store 1 [ChatRequest] for each conversation, and to
 * update the variables (especially [messages]) between [ChatBot.generateResponse]
 * calls. You can easily store your [ChatRequest] as a string or as a json file
 * using google's GSON library to serialize the object as a JSON string.
 *
 * You should not set [stream]. The stream option is handled using [ChatBot.streamResponse].
 * This allows developers to get tokens in real time instead of all at once
 * and after a long delay.
 *
 * @property model            The model used to generate the text. Recommended: `"gpt-3.5-turbo"` (without quotes).
 * @property messages         A mutable list of previous messages from the conversation.
 * @property temperature      How "creative" the results are. [0.0, 2.0]. Defaults to `1.0`.
 * @property topP             Controls how "on topic" the tokens are. Defaults to `1.0`.
 * @property n                Controls how many responses to generate. Numbers >1 will chew through your tokens. Defaults to `1`.
 * @property stream           true=wait until entire message is generated, false=respond procedurally. Defaults to `false`.
 * @property stop             The sequence used to stop generating tokens. Defaults to `null`.
 * @property maxTokens        The maximum number of tokens to use. Defaults to `infinity`.
 * @property presencePenalty  Prevent talking about duplicate topics. Defaults to `0.0`.
 * @property frequencyPenalty Prevent repeating the same text. Defaults to `0.0`.
 * @property logitBias        Increase/Decrease the chances of a specific token to appear in generated text. Defaults to `null`.
 * @property user             Who sent this request (for moderation).
 * @constructor
 * Use [builder] instead direct instantiation. This will ensure
 * backwards compatibility for Java developers.
 * @see ChatBot.generateResponse
 * @see <a href="https://platform.openai.com/docs/api-reference/completions/create">Chat API Reference</a>
 * @see <a href="https://platform.openai.com/docs/guides/chat">Chat User Reference</a>
 */
data class ChatRequest @JvmOverloads constructor(
    var model: String,
    var messages: MutableList<ChatMessage>,
    var temperature: Float? = null,
    @field:SerializedName("top_p") var topP: Float? = null,
    var n: Int? = null,
    @Deprecated("Use OpenAI#streamChatCompletion") var stream: Boolean? = null,
    var stop: String? = null,
    @field:SerializedName("max_tokens") var maxTokens: Int? = null,
    @field:SerializedName("presence_penalty") var presencePenalty: Float? = null,
    @field:SerializedName("frequency_penalty") var frequencyPenalty: Float? = null,
    @field:SerializedName("logit_bias") var logitBias: Map<String, Float>? = null,
    var user: String? = null
) {

    /**
     * [Builder] is a helper class to build a [ChatRequest] instance with a stable API.
     * It provides methods for setting the properties of the [ChatRequest] object.
     * The [build] method returns a new [ChatRequest] instance with the specified properties.
     *
     * Usage:
     * ```
     * val chatRequest = ChatRequest.builder()
     *     .model("gpt-3.5-turbo")
     *     .messages(mutableListOf("Be as helpful as possible".toSystemMessage()))
     *     .build()
     * ```
     *
     * @property model            The model used to generate the text. Recommended: `"gpt-3.5-turbo"` (without quotes).
     * @property messages         A mutable list of previous messages from the conversation.
     * @property temperature      How "creative" the results are. [0.0, 2.0]. Defaults to `1.0`.
     * @property topP             Controls how "on topic" the tokens are. Defaults to `1.0`.
     * @property n                Controls how many responses to generate. Numbers >1 will chew through your tokens. Defaults to `1`.
     * @property stream           true=wait until entire message is generated, false=respond procedurally. Defaults to `false`.
     * @property stop             The sequence used to stop generating tokens. Defaults to `null`.
     * @property maxTokens        The maximum number of tokens to use. Defaults to `infinity`.
     * @property presencePenalty  Prevent talking about duplicate topics. Defaults to `0.0`.
     * @property frequencyPenalty Prevent repeating the same text. Defaults to `0.0`.
     * @property logitBias        Increase/Decrease the chances of a specific token to appear in generated text. Defaults to `null`.
     * @property user             Who sent this request (for moderation).
     */
    class Builder {

        private lateinit var model: String
        private lateinit var messages: MutableList<ChatMessage>
        private var temperature: Float? = null
        private var topP: Float? = null
        private var n: Int? = null
        private var stop: String? = null
        private var maxTokens: Int? = null
        private var presencePenalty: Float? = null
        private var frequencyPenalty: Float? = null
        private var logitBias: Map<String, Float>? = null
        private var user: String? = null

        /**
         * Sets the model used to generate the text.
         *
         * @param model the model to be used, e.g., `"gpt-3.5-turbo"`
         * @return the [Builder] instance with the updated model property.
         */
        fun model(model: String) = apply { this.model = model }

        /**
         * Sets the messages used in the conversation.
         *
         * @param messages a mutable list of previous messages from the conversation.
         * @return the [Builder] instance with the updated messages property.
         */
        fun messages(messages: MutableList<ChatMessage>) = apply { this.messages = messages }

        /**
         * Sets the temperature for the generated text.
         *
         * @param temperature a float value representing how "creative" the results are, ranging from 0.0 to 2.0. Defaults to `1.0`.
         * @return the [Builder] instance with the updated temperature property.
         */
        fun temperature(temperature: Float?) = apply { this.temperature = temperature }

        /**
         * Sets the topP value for the generated text.
         *
         * @param topP a float value controlling how "on topic" the tokens are. Defaults to `1.0`.
         * @return the [Builder] instance with the updated topP property.
         */
        fun topP(topP: Float?) = apply { this.topP = topP }

        /**
         * Sets the number of responses to generate.
         *
         * @param n an integer value controlling how many responses to generate. Numbers >1 will consume more tokens. Defaults to `1`.
         * @return the [Builder] instance with the updated n property.
         */
        fun n(n: Int?) = apply { this.n = n }

        /**
         * Sets the stop sequence for the generated text.
         *
         * @param stop a string representing the sequence used to stop generating tokens. Defaults to `null`.
         * @return the [Builder] instance with the updated stop property.
         */
        fun stop(stop: String?) = apply { this.stop = stop }

        /**
         * Sets the maximum number of tokens for the generated text.
         *
         * @param maxTokens an integer value representing the maximum number of tokens to use. Defaults to `infinity`.
         * @return the [Builder] instance with the updated maxTokens property.
         */
        fun maxTokens(maxTokens: Int?) = apply { this.maxTokens = maxTokens }

        /**
         * Sets the presence penalty for the generated text.
         *
         * @param presencePenalty a float value to prevent talking about duplicate topics. Defaults to `0.0`.
         * @return the [Builder] instance with the updated presencePenalty property.
         */
        fun presencePenalty(presencePenalty: Float?) = apply { this.presencePenalty = presencePenalty }

        /**
         * Sets the frequency penalty for the generated text.
         *
         * @param frequencyPenalty a float value to prevent repeating the same text. Defaults to `0.0`.
         * @return the [Builder] instance with the updated frequencyPenalty property.
         */
        fun frequencyPenalty(frequencyPenalty: Float?) = apply { this.frequencyPenalty = frequencyPenalty }

        /**
         * Sets the logit bias for the generated text.
         *
         * @param logitBias a map of strings to float values to increase/decrease the chances of specific tokens appearing in the generated text. Defaults to `null`.
         * @return the [Builder] instance with the updated logitBias property.
         */
        fun logitBias(logitBias: Map<String, Float>?) = apply { this.logitBias = logitBias }

        /**
         * Sets the user who sent the request (for moderation purposes).
         *
         * @param user a string representing the user who sent the request
         * @param user a string representing the user who sent the request.
         * @return the [Builder] instance with the updated user property.
         */
        fun user(user: String?) = apply { this.user = user }

        /**
         * Constructs a new [ChatRequest] instance with the properties set in the [Builder].
         *
         * @return a new [ChatRequest] instance with the specified properties.
         */
        fun build(): ChatRequest {
            return ChatRequest(
                model = model,
                messages = messages,
                temperature = temperature,
                topP = topP,
                n = n,
                stream = false,
                stop = stop,
                maxTokens = maxTokens,
                presencePenalty = presencePenalty,
                frequencyPenalty = frequencyPenalty,
                logitBias = logitBias,
                user = user
            )
        }
    }

    companion object {

        /**
         * A static method that provides a new [Builder] instance for the
         * [ChatRequest] class.
         *
         * @return a new [Builder] instance for creating a [ChatRequest] object.
         */
        @JvmStatic
        fun builder(): Builder = Builder()
    }
}