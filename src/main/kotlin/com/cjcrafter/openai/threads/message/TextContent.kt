package com.cjcrafter.openai.threads.message

/**
 * Represents the text output of a message.
 *
 * Note that you may need to handle text annotations. Check out [TextAnnotation]
 * for more information.
 *
 * @property text The text content of the message
 */
data class TextContent(
    val text: Text
) : ThreadMessageContent() {

    override val type = Type.TEXT

    /**
     * Represents the text content of a message.
     *
     * @property value The text content of the message
     * @property annotations The annotations of the text content
     */
    data class Text(
        val value: String,
        val annotations: List<TextAnnotation>
    )
}