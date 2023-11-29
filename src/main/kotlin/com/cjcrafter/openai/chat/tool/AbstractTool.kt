package com.cjcrafter.openai.chat.tool

/**
 * Represents a tool that can be used by ChatGPT in a chat completion.
 */
abstract class AbstractTool {

    /**
     * What type of tool this is. This will always match this class's type. For
     * example, for functions:
     * ```
     *  a.getToolType() == ToolType.FUNCTION
     *  # implies
     *  a is FunctionTool  // instanceof in Java
     * ```
     */
    abstract fun getToolType(): ToolType

    /**
     * Wraps this tool in a [Tool] object for use by the OpenAI API.
     */
    fun toTool(): Tool {
        return when (val type = getToolType()) {
            ToolType.FUNCTION -> Tool(type, function=this as FunctionTool)
        }
    }
}