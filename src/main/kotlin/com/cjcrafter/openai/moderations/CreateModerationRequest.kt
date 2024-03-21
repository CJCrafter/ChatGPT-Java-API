package com.cjcrafter.openai.moderations

import com.cjcrafter.openai.util.OpenAIDslMarker

/**
 * Represents a request to create a new moderation request.
 *
 * @property input The input to moderate
 * @property model The model to use for moderation
 */
data class CreateModerationRequest internal constructor(
    var input: Any,
    var model: String? = null
) {

    @OpenAIDslMarker
    class Builder internal constructor() {
        private var input: Any? = null
        private var model: String? = null

        /**
         * Sets the input to moderate.
         *
         * @param input The input to moderate
         */
        fun input(input: String) = apply { this.input = input }

        /**
         * Sets the input to moderate.
         *
         * @param input The input to moderate
         */
        fun input(input: List<String>) = apply { this.input = input }

        /**
         * Sets the model to use for moderation.
         *
         * @param model The model to use for moderation
         */
        fun model(model: String) = apply { this.model = model }

        /**
         * Builds the [CreateModerationRequest] instance.
         */
        fun build(): CreateModerationRequest {
            return CreateModerationRequest(
                input = input ?: throw IllegalStateException("input must be defined to use CreateModerationRequest"),
                model = model
            )
        }
    }

    companion object {
        /**
         * Returns a builder to construct a [CreateModerationRequest] instance.
         */
        @JvmStatic
        fun builder() = Builder()
    }
}