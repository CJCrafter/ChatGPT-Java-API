package com.cjcrafter.openai.chat.tool

/**
 * Creates a [FunctionTool] using the [FunctionTool.Builder] using Kotlin DSL.
 */
fun functionTool(init: Function.Builder.() -> Unit) = Function.builder().apply(init).build()