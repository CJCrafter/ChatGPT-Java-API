package com.cjcrafter.openai.embeddings

/**
 * Creates an [EmbeddingsRequest] using the [EmbeddingsRequest.Builder] using Kotlin DSL.
 */
fun embeddingsRequest(block: EmbeddingsRequest.Builder.() -> Unit) = EmbeddingsRequest.builder().apply(block).build()