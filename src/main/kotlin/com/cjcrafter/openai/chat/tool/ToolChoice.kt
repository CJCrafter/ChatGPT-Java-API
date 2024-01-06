package com.cjcrafter.openai.chat.tool

import com.cjcrafter.openai.chat.ChatRequest
import com.cjcrafter.openai.jackson.ToolChoiceDeserializer
import com.cjcrafter.openai.jackson.ToolChoiceSerializer

/**
 * Sometimes, you may want chat to be forced to use a tool. Sometimes you may
 * want to prevent chat from using a tool. This sealed class represents all
 * options that can be used with the Chat endpoint.
 *
 * In general, you should use [ToolChoice.Auto] unless you have a specific
 * reason not to.
 *
 * Use the helper methods in the chat request builder:
 * * [ChatRequest.Builder.useAutoTool] -> [ToolChoice.Auto]
 * * [ChatRequest.Builder.useNoTool] -> [ToolChoice.None]
 * * [ChatRequest.Builder.useFunctionTool] -> [ToolChoice.Function]
 */
sealed class ToolChoice {

    /**
     * Lets ChatGPT automatically decide whether to use a tool, and which tool
     * to use. This is the default value (when `toolChoice` is `null`).
     */
    data object Auto : ToolChoice()

    /**
     * Prevents ChatGPT from using any tool.
     */
    data object None : ToolChoice()

    /**
     * Forces ChatGPT to use the specified function.
     *
     * @property name The name of the function to call
     */
    data class Function(var name: String) : ToolChoice()

    companion object {
        @JvmStatic
        fun serializer() = ToolChoiceSerializer()

        @JvmStatic
        fun deserializer() = ToolChoiceDeserializer()
    }
}