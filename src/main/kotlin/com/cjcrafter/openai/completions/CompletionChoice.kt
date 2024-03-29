package com.cjcrafter.openai.completions

import com.cjcrafter.openai.FinishReason
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The OpenAI API returns a list of `CompletionChoice`. Each choice has a
 * generated message ([CompletionChoice.text]) and a finish reason
 * ([CompletionChoice.finishReason]). For most use cases, you only need the
 * generated text.
 *
 * By default, only 1 choice is generated (since [CompletionRequest.n] == 1).
 * When you increase `n` or provide a list of prompts (called batching),
 * there will be multiple choices.
 *
 * @property text The generated text.
 * @property index The index in the list... This is 0 for most use cases.
 * @property logprobs List of logarithmic probabilities for each token in the generated text.
 * @property finishReason The reason the bot stopped generating tokens.
 * @constructor Create empty Completion choice, for internal usage.
 * @see FinishReason
 */
data class CompletionChoice(
    @JsonProperty(required = true) val text: String,
    @JsonProperty(required = true) val index: Int,
    @JsonProperty(required = true) val logprobs: List<Float>?,
    @JsonProperty("finish_reason", required = true) val finishReason: FinishReason
)
