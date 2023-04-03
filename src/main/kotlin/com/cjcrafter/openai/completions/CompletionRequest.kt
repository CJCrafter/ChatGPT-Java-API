package com.cjcrafter.openai.completions

import com.google.gson.annotations.SerializedName

/**
 * `CompletionRequest` holds the configurable options that can be sent to the OpenAI
 * Completions API. For most use cases, you only need to set [model] and [prompt].
 * For more detailed descriptions for each option, refer to the
 * [Completions Wiki](https://platform.openai.com/docs/api-reference/completions/create).
 *
 * [prompt] can be either a singular `String`, a `List<String>`, or a `String[]`.
 * Providing multiple prompts is called [batching](https://platform.openai.com/docs/guides/rate-limits/batching-requests),
 * and it can be used to reduce rate limit errors. This will cause the [CompletionResponse]
 * to have multiple choices.
 *
 * You should not set [stream]. The stream option is handled using []
 *
 * @property model ID of the model to use.
 * @property prompt The prompt(s) to generate completions for (either a `String`, `List<String>`, or `String[]`)
 * @property suffix The suffix that comes after a completion of inserted text.
 * @property maxTokens The maximum number of tokens to generate in the completion.
 * @property temperature What sampling temperature to use, between 0 and 2.
 * @property topP An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of the tokens with top_p probability mass.
 * @property n How many completions to generate for each prompt.
 * @property stream Whether to stream back partial progress.
 * @property logprobs Include the log probabilities on the logprobs most likely tokens, as well the chosen tokens.
 * @property echo Echo back the prompt in addition to the completion.
 * @property stop Up to 4 sequences where the API will stop generating further tokens.
 * @property presencePenalty Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far, increasing the model's likelihood to talk about new topics.
 * @property frequencyPenalty Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far, decreasing the model's likelihood to repeat the same line verbatim.
 * @property bestOf Generates best_of completions server-side and returns the "best" (the one with the highest log probability per token).
 * @property logitBias Modify the likelihood of specified tokens appearing in the completion.
 * @property user A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse.
 * @constructor Create a CompletionRequest instance. Recommend using [builder] instead.
 */
data class CompletionRequest @JvmOverloads constructor(
    var model: String,
    var prompt: Any? = null,
    var suffix: String? = null,
    @field:SerializedName("max_tokens") var maxTokens: Int? = null,
    var temperature: Number? = null,
    @field:SerializedName("top_p") var topP: Number? = null,
    var n: Int? = null,
    @Deprecated("Use OpenAI#streamCompletion") var stream: Boolean? = null,
    var logprobs: Int? = null,
    var echo: Boolean? = null,
    var stop: Any? = null,
    @field:SerializedName("presence_penalty") var presencePenalty: Number? = null,
    @field:SerializedName("frequency_penalty") var frequencyPenalty: Number? = null,
    @field:SerializedName("best_of") var bestOf: Int? = null,
    @field:SerializedName("logit_bias") var logitBias: Map<String, Int>? = null,
    var user: String? = null
) {

    /**
     * `Builder` is a helper class to build a [CompletionRequest] instance with a
     * stable API. It provides methods for setting the properties fo the [CompletionRequest]
     * object. The [build] method returns a new [CompletionRequest] instance with the
     * specified properties.
     *
     * Usage:
     * ```
     * val completionRequest = CompletionRequest.builder()
     *      .model("davinci")
     *      .prompt("The wheels on the bus go")
     *      .build()
     * ```
     */
    class Builder {
        private var model: String? = null
        private var prompt: Any? = null
        private var suffix: String? = null
        private var maxTokens: Int? = null
        private var temperature: Number? = null
        private var topP: Number? = null
        private var n: Int? = null
        private var stream: Boolean? = null
        private var logprobs: Int? = null
        private var echo: Boolean? = null
        private var stop: Any? = null
        private var presencePenalty: Number? = null
        private var frequencyPenalty: Number? = null
        private var bestOf: Int? = null
        private var logitBias: Map<String, Int>? = null
        private var user: String? = null

        /**
         * Sets the model for the CompletionRequest.
         * @param model The ID of the model to use.
         * @return The updated Builder instance.
         */
        fun model(model: String) = apply { this.model = model }

        /**
         * Sets the prompt for the CompletionRequest.
         * @param prompt The prompt to generate completions for, encoded as a string.
         * @return The updated Builder instance.
         */
        fun prompt(prompt: String?) = apply { this.prompt = prompt }

        /**
         * Sets the list of prompts for the CompletionRequest.
         * @param prompts The prompts to generate completions for, encoded as a list of strings.
         * @return The updated Builder instance.
         */
        fun prompts(prompts: List<String>?) = apply { this.prompt = prompts }

        /**
         * Sets the array of prompts for the CompletionRequest.
         * @param prompts The prompts to generate completions for, encoded as an array of strings.
         * @return The updated Builder instance.
         */
        fun prompts(prompts: Array<String>?) = apply { this.prompt = prompts }

        /**
         * Sets the suffix for the CompletionRequest.
         * @param suffix The suffix that comes after a completion of inserted text.
         * @return The updated Builder instance.
         */
        fun suffix(suffix: String?) = apply { this.suffix = suffix }

        /**
         * Sets the maximum number of tokens for the CompletionRequest.
         * @param maxTokens The maximum number of tokens to generate in the completion.
         * @return The updated Builder instance.
         */
        fun maxTokens(maxTokens: Int?) = apply { this.maxTokens = maxTokens }

        /**
         * Sets the temperature for the CompletionRequest.
         * @param temperature What sampling temperature to use, between 0 and 2.
         * @return The updated Builder instance.
         */
        fun temperature(temperature: Number?) = apply { this.temperature = temperature }

        /**
         * Sets the top_p for the CompletionRequest.
         * @param topP An alternative to sampling with temperature, called nucleus sampling.
         * @return The updated Builder instance.
         */
        fun topP(topP: Number?) = apply { this.topP = topP }

        /**
         * Sets the number of completions for the CompletionRequest.
         * @param n How many completions to generate for each prompt.
         * @return The updated Builder instance.
         */
        fun n(n: Int?) = apply { this.n = n }

        /**
         * Sets the stream option for the CompletionRequest.
         * @param stream Whether to stream back partial progress.
         * @return The updated Builder instance.
         */
        fun stream(stream: Boolean?) = apply { this.stream = stream }

        /**
         * Sets the logprobs for the CompletionRequest.
         * @param logprobs Include the log probabilities on the logprobs most likely tokens, as well as the chosen tokens.
         * @return The updated Builder instance.
         */
        fun logprobs(logprobs: Int?) = apply { this.logprobs = logprobs }

        /**
         * Sets the echo option for the CompletionRequest.
         * @param echo Echo back the prompt in addition to the completion.
         * @return The updated Builder instance.
         */
        fun echo(echo: Boolean?) = apply { this.echo = echo }

        /**
         * Sets the stop sequences for the CompletionRequest.
         * @param stop Up to 4 sequences where the API will stop generating further tokens.
         * @return The updated Builder instance.
         */
        fun stop(stop: Any?) = apply { this.stop = stop }

        /**
         * Sets the presence penalty for the CompletionRequest.
         * @param presencePenalty Number between -2.0 and 2.0 for penalizing new tokens based on whether they appear in the text so far.
         * @return The updated Builder instance.
         */
        fun presencePenalty(presencePenalty: Number?) = apply { this.presencePenalty = presencePenalty }

        /**
         * Sets the frequency penalty for the CompletionRequest.
         * @param frequencyPenalty Number between -2.0 and 2.0 for penalizing new tokens based on their existing frequency in the text so far.
         * @return The updated Builder instance.
         */
        fun frequencyPenalty(frequencyPenalty: Number?) = apply { this.frequencyPenalty = frequencyPenalty }

        /**
         * Sets the best_of option for the CompletionRequest.
         * @param bestOf Generates best_of completions server-side and returns the "best" (the one with the highest log probability per token).
         * @return The updated Builder instance.
         */
        fun bestOf(bestOf: Int?) = apply { this.bestOf = bestOf }

        /**
         * Sets the logit bias for the CompletionRequest.
         * @param logitBias Modify the likelihood of specified tokens appearing in the completion.
         * @return The updated Builder instance.
         */
        fun logitBias(logitBias: Map<String, Int>?) = apply { this.logitBias = logitBias }

        /**
         * Sets the user identifier for the CompletionRequest.
         * @param user A unique identifier representing your end-user, which can help OpenAI to monitor and detect abuse.
         * @return The updated Builder instance.
         */
        fun user(user: String?) = apply { this.user = user }

        /**
         * Builds the CompletionRequest instance with the provided parameters.
         *
         * @return The constructed CompletionRequest instance.
         */
        fun build(): CompletionRequest {
            require(model != null) { "Set CompletionRequest.Builder#model(String) before building" }

            return CompletionRequest(
                model = model!!,
                prompt = prompt,
                suffix = suffix,
                maxTokens = maxTokens,
                temperature = temperature,
                topP = topP,
                n = n,
                stream = stream,
                logprobs = logprobs,
                echo = echo,
                stop = stop,
                presencePenalty = presencePenalty,
                frequencyPenalty = frequencyPenalty,
                bestOf = bestOf,
                logitBias = logitBias,
                user = user
            )
        }
    }

    companion object {

        /**
         * A static method that provides a new [Builder] instance for the
         * [CompletionRequest] class.
         *
         * @return a new [Builder] instance for creating a [CompletionRequest] object.
         */
        @JvmStatic
        fun builder(): Builder = Builder()
    }
}

