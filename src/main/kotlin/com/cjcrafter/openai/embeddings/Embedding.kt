package com.cjcrafter.openai.embeddings

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents 1 embedding as a vector of floats or strings.
 *
 * @property embedding The embedding as a list of floats or strings
 * @property index The index of the embedding in the list of embeddings
 */
data class Embedding(
    @JsonProperty(required = true) val embedding: List<Any>,
    @JsonProperty(required = true) val index: Int,
) {
    /**
     * Returns the embedding as a list of floats. Make sure to use [EncodingFormat.FLOAT].
     */
    fun asDoubles() = embedding.map { it as Double }

    /**
     * Returns the embedding as a list of strings. Make sure to use [EncodingFormat.BASE64].
     */
    fun asBase64() = embedding.map { it as String }
}