package com.cjcrafter.openai.embeddings

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Holds the number of tokens used by the API request. Exact pricing can vary
 * based on the model used.
 *
 * @property promptTokens How many tokens were taken by the input strings
 * @property totalTokens The total number of tokens used
 */
data class EmbeddingsUsage(
    @JsonProperty("prompt_tokens") val promptTokens: Int,
    @JsonProperty("total_tokens") val totalTokens: Int,
)