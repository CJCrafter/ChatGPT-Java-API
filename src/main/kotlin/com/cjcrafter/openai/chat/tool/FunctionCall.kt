package com.cjcrafter.openai.chat.tool

import com.cjcrafter.openai.exception.HallucinationException
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jetbrains.annotations.ApiStatus

/**
 * Represents a function call by ChatGPT. When ChatGPT calls a function, you
 * should be parsing the arguments, calling some method (getting current weather
 * at a location, getting a stock price, modifying a database, etc.), and then
 * replying to ChatGPT with the result.
 *
 * ChatGPT may *hallucinate*, or make up, function calls. To handle hallucinations,
 * we have provided [tryParseArguments].
 *
 * @property name The name of the function which was called
 * @property arguments The raw json representation of the arguments
 */
data class FunctionCall(
    var name: String,
    var arguments: String,
) {
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
            parameters = tools.find { it.type == ToolType.FUNCTION && it.function.name == name }?.function?.parameters
                ?: throw HallucinationException("Unknown function: $name")
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
                            "enum" -> if (!value.isTextual)
                                throw HallucinationException("Expected a string for argument $key")
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