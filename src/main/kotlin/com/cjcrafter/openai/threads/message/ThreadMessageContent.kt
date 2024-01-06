package com.cjcrafter.openai.threads.message

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * A sealed class which represents a message in a thread.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(TextContent::class, name = "text"),
    JsonSubTypes.Type(ImageContent::class, name = "image_file"),
)
sealed class ThreadMessageContent {

    /**
     * The type of content.
     */
    abstract val type: Type

    enum class Type {

        /**
         * A message containing text, usually as a markdown string.
         *
         * @see TextContent
         */
        TEXT,

        /**
         * A message containing an image, stored as a file id.
         *
         * @see ImageContent
         */
        IMAGE_FILE,
    }
}