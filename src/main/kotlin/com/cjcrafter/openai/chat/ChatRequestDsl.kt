package com.cjcrafter.openai.chat

import com.cjcrafter.openai.chat.tool.FunctionTool

/**
 * Creates a [ChatRequest] using the [ChatRequest.Builder] using Kotlin DSL.
 */
fun chatRequest(block: ChatRequest.Builder.() -> Unit) = ChatRequest.builder().apply(block).build()

/**
 * Adds a [FunctionTool] to the [ChatRequest] using Kotlin DSL.
 */
fun ChatRequest.Builder.function(block: FunctionTool.Builder.() -> Unit) = addTool(FunctionTool.builder().apply(block).build())