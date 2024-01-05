package com.cjcrafter.openai.threads.message

data class TextContent(
    val text: Text
) : ThreadMessageContent() {

    override val type = Type.TEXT

    data class Text(
        val value: String,
        val annotations: List<TextAnnotation>
    )
}