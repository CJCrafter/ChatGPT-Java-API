package com.cjcrafter.openai.chat.tool

data class ToolCall(
    var id: String,
    var type: ToolType,
    var function: FunctionCall,
) {
}