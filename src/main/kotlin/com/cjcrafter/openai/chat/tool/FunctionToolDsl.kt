package com.cjcrafter.openai.chat.tool

/**
 * Creates a [FunctionTool] using the [FunctionTool.Builder] using Kotlin DSL.
 */
fun functionTool(init: FunctionTool.Builder.() -> Unit) = FunctionTool.builder().apply(init).build()