package com.cjcrafter.openai.completions

/**
 * Creates a [CompletionRequest] using the [CompletionRequest.Builder] using Kotlin DSL.
 */
fun completionRequest(block: CompletionRequest.Builder.() -> Unit) = CompletionRequest.builder().apply(block).build()