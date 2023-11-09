package com.cjcrafter.openai.chat.tool

/**
 * Wraps a tool call by ChatGPT. You should check the [type] of the tool call,
 * and handle the request. For example, if the type is [ToolType.FUNCTION], you
 * should call the function and return the result.
 *
 * When making subsequent requests to chat completions, you should make sure to
 * pass the message that contained this tool call, and the result of the tool
 * call.
 *
 * @property id The id of this call. You should use this to construct a [com.cjcrafter.openai.chat.ChatUser.TOOL] message.
 * @property type The type of tool call. Currently, the only type is [ToolType.FUNCTION].
 * @property function The function call containing the function name and arguments.
 */
data class ToolCall(
    var id: String,
    var type: ToolType,
    var function: FunctionCall,
) {
    internal fun update(delta: ToolCallDelta) {
        // The only field that updates is function
        if (delta.function != null)
            function.update(delta.function)
    }
}