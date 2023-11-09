package com.cjcrafter.openai.chat.tool

/**
 * Represents the "delta," or changes of a function call. This is used by streams
 * to stream 1 token at a time.
 *
 * @property name The name of the function to call. Will always be `null` except for the first call.
 * @property arguments 1 token of the arguments. Well be delivered as a JSON string.
 */
data class FunctionCallDelta(
    val name: String?,
    val arguments: String,
) {

    /**
     * Returns an **incomplete** function call.
     */
    internal fun toFunctionCall(): FunctionCall {
        return FunctionCall(
            name ?: throw IllegalStateException("name must be set"),
            arguments
        )
    }
}