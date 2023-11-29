package com.cjcrafter.openai.threads.message

data class TextContent(
    val text: Text
) : ThreadMessageContent() {

    data class Text(
        val value: String,
        val annotations: List<TextAnnotation>
    )
}