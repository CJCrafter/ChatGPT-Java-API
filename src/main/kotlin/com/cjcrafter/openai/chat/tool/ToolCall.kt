package com.cjcrafter.openai.chat.tool

import org.jetbrains.annotations.ApiStatus

data class ToolCall(
    var id: String,
    var type: ToolType,
    var function: FunctionCall,
) {
    @ApiStatus.Internal
    internal fun update(delta: ToolCallDelta) {
        // The only field that updates is function
        if (delta.function != null)
            function.update(delta.function)
    }
}