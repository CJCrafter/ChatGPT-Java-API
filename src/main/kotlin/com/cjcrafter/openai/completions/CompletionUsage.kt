package com.cjcrafter.openai.completions

import com.google.gson.annotations.SerializedName

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
 */
data class CompletionUsage(
    @field:SerializedName("prompt_tokens") val promptTokens: Int,
    @field:SerializedName("completion_tokens") val completionTokens: Int,
    @field:SerializedName("total_tokens") val totalTokens: Int
)