package com.cjcrafter.openai.chat

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Holds how many tokens that were used by your API request. Use these
 * tokens to calculate how much money you have spent on each request.
 *
 * By monitoring your token usage, you can limit the amount of money charged
 * for your requests. You can check the cost of the model you are using in
 * OpenAI's billing page.
 *
 * @param promptTokens     How many tokens the input used.
 * @param completionTokens How many tokens the output used.
 * @param totalTokens      How many tokens in total.
 * @see <a href="https://platform.openai.com/docs/guides/chat/managing-tokens">Managing Tokens Guide</a>
 */
data class ChatUsage(
    @JsonProperty("prompt_tokens", required = true) val promptTokens: Int,
    @JsonProperty("completion_tokens", required = true) val completionTokens: Int,
    @JsonProperty("total_tokens", required = true) val totalTokens: Int
)