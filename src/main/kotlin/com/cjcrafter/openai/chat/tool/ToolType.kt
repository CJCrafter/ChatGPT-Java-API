package com.cjcrafter.openai.chat.tool

import com.google.gson.annotations.SerializedName

enum class ToolType {

    /**
     * A tool that calls a function.
     *
     * @see FunctionTool
     */
    @SerializedName("function")
    FUNCTION;
}