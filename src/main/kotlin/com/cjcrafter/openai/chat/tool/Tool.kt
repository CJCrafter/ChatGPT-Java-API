package com.cjcrafter.openai.chat.tool

import com.cjcrafter.openai.assistants.Assistant
import com.cjcrafter.openai.threads.message.TextAnnotation
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * Represents a tool that can be used by ChatGPT in a chat completion.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(Tool.FunctionTool::class, name = "function"),
    JsonSubTypes.Type(Tool.RetrievalTool::class, name = "retrieval"),
    JsonSubTypes.Type(Tool.CodeInterpreterTool::class, name = "code_interpreter"),
)
sealed class Tool {

    /**
     * Represents a tool that calls a function.
     *
     * @see Function.builder
     */
    data class FunctionTool(
        @JsonProperty(required = true) var function: Function,
    ): Tool() {
        override val type = Type.FUNCTION
    }

    /**
     * Represents a tool that retrieves data from uploaded files.
     *
     * Note that retrieval tools are only supported by [Assistant]s
     */
    data object RetrievalTool: Tool() {
        override val type = Type.RETRIEVAL
    }

    /**
     * Represents a tool that runs Python code on the OpenAI server.
     *
     * Note that code interpreter tools are only supported by [Assistant]s
     */
    data object CodeInterpreterTool: Tool() {
        override val type = Type.CODE_INTERPRETER
    }

    /**
     * Represents the type of the tool.
     */
    enum class Type {

        /**
         * A tool that calls a function.
         *
         * @see FunctionTool
         */
        @JsonProperty("function")
        FUNCTION,

        /**
         * A tool that retrieves data from uploaded files.
         *
         * Note that retrieval tools are only supported by [Assistant]s
         */
        @JsonProperty("retrieval")
        RETRIEVAL,

        /**
         * A tool that runs Python code on the OpenAI server.
         *
         * Note that code interpreter tools are only supported by [Assistant]s
         */
        @JsonProperty("code_interpreter")
        CODE_INTERPRETER,
    }

    /**
     * What type of tool this is. This will always match this class's type. For
     * example, for functions:
     * ```
     *  a.getToolType() == ToolType.FUNCTION
     *  # implies
     *  a is FunctionTool  // instanceof in Java
     * ```
     */
    abstract val type: Type
}