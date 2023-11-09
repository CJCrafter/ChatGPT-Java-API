package com.cjcrafter.openai.chat.tool

import org.jetbrains.annotations.ApiStatus

data class ToolCallDelta(
    val index: Int,
    val id: String? = null,
    val type: ToolType? = null,
    val function: FunctionCallDelta? = null,
) {
    @ApiStatus.Internal
    fun toToolCall() = ToolCall(
        id = id ?: throw IllegalStateException("id must be set"),
        type = type ?: throw IllegalStateException("type must be set"),
        function = function?.toFunctionCall() ?: throw IllegalStateException("function must be set"),
    )
}