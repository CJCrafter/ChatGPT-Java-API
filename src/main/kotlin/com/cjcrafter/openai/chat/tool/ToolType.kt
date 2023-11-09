package com.cjcrafter.openai.chat.tool

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the type of tool. Currently, the only type of tool is a function.
 * In the future, this may include Data Analysis and DALL-E.
 */
enum class ToolType {

    /**
     * A tool that calls a function.
     *
     * @see FunctionTool
     */
    @JsonProperty("function")
    FUNCTION;
}