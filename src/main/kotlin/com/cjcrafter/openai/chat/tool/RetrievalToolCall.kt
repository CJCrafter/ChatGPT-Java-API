package com.cjcrafter.openai.chat.tool

import com.fasterxml.jackson.annotation.JsonProperty
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a tool call for [Tool.Type.RETRIEVAL].
 *
 * Currently, this tool call serves no functionality other than to inform you
 * that some retrieval operation has occurred. Currently, the OpenAI API does
 * not return any information about the retrieval ([retrieval] will always be
 * an empty map. [retrieval]'s data type may be changed in the future, I
 * recommend you __DO NOT__ use it).
 *
 * @property id The unique id of this tool call
 * @property retrieval An empty map, do not use this!
 */
data class RetrievalToolCall(
    @JsonProperty(required = true) override val id: String,
    @JsonProperty(required = true) @ApiStatus.Experimental val retrieval: Map<String, String>,
): ToolCall() {
    override val type = Tool.Type.RETRIEVAL
}