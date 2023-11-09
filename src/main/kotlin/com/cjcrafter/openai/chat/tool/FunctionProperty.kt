package com.cjcrafter.openai.chat.tool

/**
 * Represents 1 single parameter from a map of parameters for a function.
 *
 * You probably shouldn't use this class directly. Instead, use [FunctionTool.builder].
 *
 * @property type The data type of the parameter. Usually either string or integer.
 * @property description A description of the parameter. This is optional.
 * @property enum A list of possible values for the parameter. This is optional.
 */
data class FunctionProperty(
    var type: String,
    var description: String? = null,
    var enum: MutableList<String>? = null,
)