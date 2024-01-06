package com.cjcrafter.openai

import org.jetbrains.annotations.Contract

/**
 * Builds an [OpenAI] instance using the default implementation.
 */
@Contract(pure = true)
fun openAI(init: OpenAI.Builder.() -> Unit) = OpenAI.builder().apply(init).build()

/**
 * Builds an [OpenAI] instance using the Azure implementation.
 */
@Contract(pure = true)
fun azureOpenAI(init: OpenAI.AzureBuilder.() -> Unit) = OpenAI.azureBuilder().apply(init).build()