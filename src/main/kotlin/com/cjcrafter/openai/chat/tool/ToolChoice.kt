package com.cjcrafter.openai.chat.tool

import com.cjcrafter.openai.jackson.ToolChoiceDeserializer
import com.cjcrafter.openai.jackson.ToolChoiceSerializer

sealed class ToolChoice {

    /**
     * Lets ChatGPT automatically decide whether to use a tool, and which tool
     * to use. This is the default value (when `toolChoice` is `null`).
     */
    object Auto : ToolChoice()

    /**
     * Prevents ChatGPT from using any tool.
     */
    object None : ToolChoice()

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