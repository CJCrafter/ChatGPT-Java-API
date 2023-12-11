package com.cjcrafter.openai.chat.tool

import com.cjcrafter.openai.OpenAI
import com.cjcrafter.openai.assistants.Assistant
import com.cjcrafter.openai.exception.HallucinationException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

/**
 * Represents a function call by either a chat completion, or an [Assistant].
 *
 * When a function call is made, you MUST respond with the result of the function.
 * This means that you should parse the arguments, call some function (an API call,
 * getting current weather, getting a stock price, modifying a database, etc.), and
 * sending the result of that function back.
 *
 * For chat completions ([OpenAI.createChatCompletion]), you should send the result
 * of the function as a [com.cjcrafter.openai.chat.ChatMessage] with the
 * corresponding [ToolCall.id] as the function id.
 *
 * For [Assistant]s, you should use [com.cjcrafter.openai.threads.runs.RunHandler.submitToolOutputs]
 * with the corresponding [ToolCall.id] as the tool call id. For [Assistant]s,
 * it is important to submit tool outputs _within a timely manner_, usually
 * within 10 minutes of starting a [com.cjcrafter.openai.threads.runs.Run].
 * Otherwise, the [com.cjcrafter.openai.threads.runs.Run] will expire, and you
 * will not be able to submit your tool call.
 *
 * ChatGPT may _hallucinate_, or make up, function calls. To handle hallucinations,
 * we have provided [tryParseArguments].
 *
 * @property name The name of the function which was called
 * @property arguments The raw json representation of the arguments
 * @property output The result of the function call if it has been set, only used for [Assistant]s. You should not set this.
 */
data class FunctionCall(
    var name: String,
    var arguments: String,
    var output: String? = null,
) {

    /**
     * Used internally to update the function call. This is used when the chat
     * completion is streamed via [OpenAI.streamChatCompletion]. This is not
     * used by [Assistant]s.
     */
    internal fun update(delta: FunctionCallDelta) {
        // The only field that updates is arguments
        arguments += delta.arguments
    }

    /**
     * Attempts to parse the arguments passed to the function.
     *
     * If there is a mistake in the JSON provided by ChatGPT, this method will
     * throw a [HallucinationException]. You should catch this exception and
     * return the error provided by [HallucinationException.jsonDumps]. After
     * you reply to ChatGPT with the error, it may attempt to fix its mistake,
     * or simply continue on without using the tool.
     *
     * When using [com.cjcrafter.openai.OpenAI.streamChatCompletion], you should
     * wait until the stream is closed before calling this method. You can check
     * this by checking the [com.cjcrafter.openai.chat.ChatChoiceChunk.isFinished].
     *
     * @param tools The list of tools that ChatGPT has access to, or null to skip advanced checking.
     * @return The parsed arguments.
     */
    @JvmOverloads
    @Throws(HallucinationException::class)
    fun tryParseArguments(tools: List<Tool>? = null): Map<String, JsonNode> {
        var parameters: FunctionParameters? = null
        if (tools != null) {
            val functionTool: Tool.FunctionTool = tools.find { it is Tool.FunctionTool && it.function.name == name } as? Tool.FunctionTool
                ?: throw HallucinationException("Unknown function: $name")
            parameters = functionTool.function.parameters
        }

        try {
            val mapper = jacksonObjectMapper()
            val rootNode: JsonNode = mapper.readValue(arguments)

            if (!rootNode.isObject) {
                throw HallucinationException("Expected to get a JSON object")
            }

            // Check for required parameters
            parameters?.required?.forEach { required ->
                if (!rootNode.has(required))
                    throw HallucinationException("Missing required argument: $required")
            }

            // Loop through all parameters and ensure they are valid
            return rootNode.fields().asSequence().associate { (key, value) ->
                parameters?.let { params ->
                    params[key]?.let { property ->
                        // Ensure the type is correct
                        when (property.type) {
                            "integer" -> if (!value.isInt)
                                throw HallucinationException("Expected an integer for argument $key")
                            "number" -> if (!value.isDouble && !value.isInt)
                                throw HallucinationException("Expected a number for argument $key")
                            "boolean" -> if (!value.isBoolean)
                                throw HallucinationException("Expected a boolean for argument $key")
                            "string" -> if (!value.isTextual)
                                throw HallucinationException("Expected a string for argument $key")
                            "enum" -> if (!value.isTextual || !property.enum!!.contains(value.asText()))
                                throw HallucinationException("Expected one of ${property.enum}, got $key")
                        }
                    } ?: throw HallucinationException("Unknown argument: $key")
                }
                key to value
            }
        } catch (e: JsonProcessingException) {
            throw HallucinationException("Error parsing JSON arguments: ${e.message}")
        }
    }
}