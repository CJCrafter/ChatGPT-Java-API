package com.cjcrafter.openai.chat

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

/**
 * These are the arguments that control the result of the output. For more
 * information, refer to the [OpenAI Docs](https://platform.openai.com/docs/api-reference/completions/create).
 *
 * @param model            The model used to generate the text. Recommended: "gpt-3.5-turbo."
 * @param messages         All previous messages from the conversation.
 * @param temperature      How "creative" the results are. [0.0, 2.0].
 * @param topP             Controls how "on topic" the tokens are.
 * @param n                Controls how many responses to generate. Numbers >1 will chew through your tokens.
 * @param stream           **UNTESTED** recommend keeping this false.
 * @param stop             The sequence used to stop generating tokens.
 * @param maxTokens        The maximum number of tokens to use.
 * @param presencePenalty  Prevent talking about duplicate topics.
 * @param frequencyPenalty Prevent repeating the same text.
 * @param logitBias        Control specific tokens from being used.
 * @param user             Who send this request (for moderation).
 * @see <a href="https://platform.openai.com/docs/api-reference/completions/create">OpenAI Wiki</a>
 */
data class ChatRequest @JvmOverloads constructor(
    val model: String,
    var messages: MutableList<ChatMessage>,
    val temperature: Float = 1.0f,
    @field:SerializedName("top_p") val topP: Float = 1.0f,
    val n: Int = 1,
    val stream: Boolean = false,
    val stop: String? = null,
    @field:SerializedName("max_tokens") val maxTokens: Int? = null,
    @field:SerializedName("presence_penalty") val presencePenalty: Float = 0f,
    @field:SerializedName("frequency_penalty") val frequencyPenalty: Float = 0f,
    @field:SerializedName("logit_bias") val logitBias: JsonObject? = null,
    val user: String? = null
)