package com.cjcrafter.openai.embeddings

import com.cjcrafter.openai.util.OpenAIDslMarker
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Holds the options sent to the [embeddings](https://beta.openai.com/docs/api-reference/embeddings) endpoint.
 * The generated embeddings can be used in Machine Learning models.
 *
 * [input] can be either a string or a list of strings.
 *
 * @property input The input(s) to convert to embeddings.
 * @property model Which [model](https://platform.openai.com/docs/models/embeddings) to use to generate the embeddings.
 * @property encodingFormat Determines how the embeddings are encoded. Defaults to [EncodingFormat.FLOAT].
 * @property user The user ID to associate with this request.
 * @constructor Create empty Embeddings request
 */
data class EmbeddingsRequest internal constructor(
    var input: Any,
    var model: String,
    @JsonProperty("encoding_format") var encodingFormat: EncodingFormat? = null,
    var user: String? = null,
) {

    /**
     * A builder design pattern for constructing an [EmbeddingsRequest] instance.
     */
    @OpenAIDslMarker
    class Builder internal constructor() {
        private var input: Any? = null
        private var model: String? = null
        private var encodingFormat: EncodingFormat? = null
        private var user: String? = null

        fun input(input: String) = apply { this.input = input }
        fun input(input: List<String>) = apply { this.input = input }
        fun model(model: String) = apply { this.model = model }
        fun encodingFormat(encodingFormat: EncodingFormat) = apply { this.encodingFormat = encodingFormat }
        fun user(user: String) = apply { this.user = user }

        fun build(): EmbeddingsRequest {
            return EmbeddingsRequest(
                input = input ?: throw IllegalStateException("input must be defined to use EmbeddingsRequest"),
                model = model ?: throw IllegalStateException("model must be defined to use EmbeddingsRequest"),
                encodingFormat = encodingFormat,
                user = user
            )
        }
    }

    companion object {

        /**
         * Returns a builder to construct an [EmbeddingsRequest] instance.
         */
        @JvmStatic
        fun builder() = Builder()
    }
}
