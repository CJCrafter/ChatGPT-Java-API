package com.cjcrafter.openai.chat.tool

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * Wraps a tool call by ChatGPT. You should check the [type] of the tool call,
 * and handle the request. For example, if the type is [Tool.Type.FUNCTION], you
 * should call the function and return the result.
 *
 * When making subsequent requests to chat completions, you should make sure to
 * pass the message that contained this tool call, and the result of the tool
 * call.
 *
 * @property id The id of this call. You should use this to construct a [com.cjcrafter.openai.chat.ChatUser.TOOL] message.
 * @property type The type of tool call.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(FunctionToolCall::class, name = "function"),
    JsonSubTypes.Type(RetrievalToolCall::class, name = "retrieval"),
    JsonSubTypes.Type(CodeInterpreterToolCall::class, name = "code_interpreter"),
)
sealed class ToolCall {

    abstract val id: String
    abstract val type: Tool.Type

    internal open fun update(delta: ToolCallDelta) {
        // Nothing to update
    }
}