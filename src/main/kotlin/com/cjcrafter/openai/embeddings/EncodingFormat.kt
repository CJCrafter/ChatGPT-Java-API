package com.cjcrafter.openai.embeddings

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Determines how the embeddings are encoded.
 */
enum class EncodingFormat {

    /**
     * The default encoding format.
     */
    @JsonProperty("text")
    FLOAT,

    /**
     * Encodes the embedding as a base64 string.
     */
    @JsonProperty("base64")
    BASE64
}