package com.cjcrafter.openai.chat.tool

/**
 * A tool that can be used by ChatGPT. Currently, the only type of tool is a
 * function. OpenAI will likely add more types of tools in the future. To avoid
 * breaking changes in your code, use [com.cjcrafter.openai.chat.ChatRequest.Builder.addTool]
 * instead of using this class directly.
 *
 * @property type The type of tool this is (currently only functions).
 * @property function The function. This is only used if [type] is [ToolType.FUNCTION].
 */
data class Tool(
    val type: ToolType,
    val function: FunctionTool,
)
