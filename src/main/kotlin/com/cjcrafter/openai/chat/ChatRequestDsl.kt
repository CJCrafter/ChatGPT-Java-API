package com.cjcrafter.openai.chat

import com.cjcrafter.openai.chat.tool.Function
import com.cjcrafter.openai.chat.tool.Tool

/**
 * Creates a [ChatRequest] using the [ChatRequest.Builder] using Kotlin DSL.
 */
fun chatRequest(block: ChatRequest.Builder.() -> Unit) = ChatRequest.builder().apply(block).build()

/**
 * Adds a [Tool.FunctionTool] to the [ChatRequest] using Kotlin DSL.
 */
fun ChatRequest.Builder.function(block: Function.Builder.() -> Unit) = addTool(Function.builder().apply(block).build())