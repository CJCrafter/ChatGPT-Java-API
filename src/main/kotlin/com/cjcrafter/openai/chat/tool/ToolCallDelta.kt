package com.cjcrafter.openai.chat.tool

/**
 * Represents the "delta," or changes of a tool call. This is used by streams
 * to stream 1 token at a time.
 *
 * @property index The index of the tool call we are modifying.
 * @property id The tool call id used in replies. Will always be `null` except for the first call.
 * @property type The type of tool call. Will always be `null` except for the first call.
 * @property function The modifications to the function call.
 */
data class ToolCallDelta(
    val index: Int,
    val id: String? = null,
    val type: Tool.Type? = null,
    val function: FunctionCallDelta? = null,
) {
    internal fun toToolCall() = ToolCall(
        id = id ?: throw IllegalStateException("id must be set"),
        type = type ?: throw IllegalStateException("type must be set"),
        function = function?.toFunctionCall() ?: throw IllegalStateException("function must be set"),
    )
}