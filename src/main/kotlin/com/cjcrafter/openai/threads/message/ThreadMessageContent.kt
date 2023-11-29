package com.cjcrafter.openai.threads.message

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(TextContent::class, name = "text"),
    JsonSubTypes.Type(ImageContent::class, name = "image_file"),
)
abstract class ThreadMessageContent