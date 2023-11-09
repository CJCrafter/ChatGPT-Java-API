package com.cjcrafter.openai.chat.tool

import com.cjcrafter.openai.exception.HallucinationException
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSyntaxException

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
    fun tryParseArguments(tools: List<Tool>? = null): Map<String, JsonElement> {
        val parameters = if (tools == null) null else tools.find { it.type == ToolType.FUNCTION && it.function.name == name }?.function?.parameters
            ?: throw HallucinationException("Unknown function: $name")

        try {
            // use the default Gson since we don't want any special parsing
            val gson = Gson()
            val element: JsonElement = gson.fromJson(arguments, JsonElement::class.java)
            val jsonObject = element.asJsonObject

            // Check for required parameters
            if (parameters != null) {
                for (required in parameters.required) {
                    if (!jsonObject.has(required))
                        throw HallucinationException("Missing required argument: $required")
                }
            }

            // Loop through all parameters and ensure they are valid
            return jsonObject.entrySet().associate { (key, value) ->
                if (parameters != null) {
                    val property = parameters[key] ?: throw HallucinationException("Unknown argument: $key")

                    // Ensure the type is correct
                    if (property.type == "integer" && (value !is JsonPrimitive || !value.isNumber))
                        throw HallucinationException("Expected an integer for argument $key")
                    if (property.enum != null && (value !is JsonPrimitive || !value.isString))
                        throw HallucinationException("Expected a string for argument $key")
                }

                key to value
            }
        } catch (e: JsonSyntaxException) {
            throw HallucinationException("Error parsing JSON arguments: ${e.message}")
        } catch (e: IllegalStateException) {
            throw HallucinationException("Expected to get a JSON object: ${e.message}")
        }
    }
}