package com.cjcrafter.openai.chat.tool

import org.jetbrains.annotations.ApiStatus

data class FunctionCallDelta(
    val name: String?,
    val arguments: String,
) {

    /**
     * Returns an **incomplete** function call.
     */
    @ApiStatus.Internal
    fun toFunctionCall(): FunctionCall {
        return FunctionCall(
            name ?: throw IllegalStateException("name must be set"),
            arguments
        )
    }
}