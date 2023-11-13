package com.cjcrafter.openai.embeddings

/**
 * Represents 1 embedding as a vector of floats or strings.
 *
 * @property embedding
 * @property index
 * @constructor Create empty Embedding
 */
data class Embedding(
    val embedding: List<Any>,
    val index: Int,
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