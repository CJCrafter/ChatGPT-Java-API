package com.cjcrafter.openai.embeddings

/**
 * The API response from the [EmbeddingsRequest].
 *
 * @property data The embeddings data
 * @property model The exact model used to generate the embeddings
 * @property usage How many tokens were used by the API request
 */
data class EmbeddingsResponse(
    val data: List<Embedding>,
    val model: String,
    val usage: EmbeddingsUsage,
) {
    /**
     * Returns the [data] at the given [index].
     */
    operator fun get(index: Int): Embedding {
        return data[index]
    }
}
