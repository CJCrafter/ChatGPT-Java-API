package com.cjcrafter.openai.files

/**
 * Creates an [ListFilesRequest] using the [ListFilesRequest.Builder] using Kotlin DSL.
 */
fun listFilesRequest(block: ListFilesRequest.Builder.() -> Unit) = ListFilesRequest.builder().apply(block).build()