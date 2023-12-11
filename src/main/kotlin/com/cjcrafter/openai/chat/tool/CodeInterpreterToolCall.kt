package com.cjcrafter.openai.chat.tool

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a tool call by an [com.cjcrafter.openai.assistants.Assistant]
 * to a code interpreter.
 *
 * @property id The unique id of this tool call
 * @property codeInterpreter The details about the input and output
 */
data class CodeInterpreterToolCall(
    @JsonProperty(required = true) override val id: String,
    @JsonProperty("code_interpreter", required = true) val codeInterpreter: CodeInterpreter,
) : ToolCall() {
    override val type = Tool.Type.CODE_INTERPRETER

}