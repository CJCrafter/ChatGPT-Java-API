package com.cjcrafter.openai.chat.tool

import com.fasterxml.jackson.annotation.JsonProperty

enum class ToolType {

    /**
     * A tool that calls a function.
     *
     * @see FunctionTool
     */
    @JsonProperty("function")
    FUNCTION;
}