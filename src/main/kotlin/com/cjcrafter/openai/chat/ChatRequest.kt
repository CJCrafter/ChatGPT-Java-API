package com.cjcrafter.openai.chat

import com.google.gson.annotations.SerializedName

/**
 * [ChatRequest] holds the configurable options that can be sent to the OpenAI
 * Chat API. For most use cases, you only need to set [model] and [messages].
 * For more detailed descriptions for each option, refer to the
 * [Chat Wiki](https://platform.openai.com/docs/api-reference/chat)
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
 * You should not set [stream]. TODO update docs after adding stream support
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
 * @property user             Who send this request (for moderation).
 * @constructor Create a chat request
 * @see ChatBot.generateResponse
 * @see <a href="https://platform.openai.com/docs/api-reference/completions/create">OpenAI Wiki</a>
 */
data class ChatRequest @JvmOverloads constructor(
    var model: String,
    var messages: MutableList<ChatMessage>,
    var temperature: Float? = null,
    @field:SerializedName("top_p") var topP: Float? = null,
    var n: Int? = null,
    var stream: Boolean? = null,
    var stop: String? = null,
    @field:SerializedName("max_tokens") var maxTokens: Int? = null,
    @field:SerializedName("presence_penalty") var presencePenalty: Float? = null,
    @field:SerializedName("frequency_penalty") var frequencyPenalty: Float? = null,
    @field:SerializedName("logit_bias") var logitBias: Map<String, Float>? = null,
    var user: String? = null
)