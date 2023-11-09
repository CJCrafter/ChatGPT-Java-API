package com.cjcrafter.openai.chat.tool

/**
 * Represents the parameters of the function. You can think of this like a
 * method signature, where each property is a parameter to a method.
 *
 * This class should **not** be used directly. Instead, use the [FunctionTool.builder]
 * to add parameters.
 *
 * @property type The type of the parameters. This should always be "object".
 * @property properties The map of method parameters.
 */
data class FunctionParameters internal constructor(
    var type: String = "object",
    var properties: MutableMap<String, FunctionProperty> = mutableMapOf(),
    var required: MutableSet<String> = mutableSetOf(),
) {
    /**
     * Require that the given parameter is used by ChatGPT.
     *
     * @param name The name of the parameter
     */
    fun require(name: String) {
        if (!properties.contains(name))
            throw IllegalArgumentException("Parameter $name does not exist. Please add the parameter before requiring it")

        required.add(name)
    }

    operator fun get(name: String): FunctionProperty? {
        return properties[name]
    }

    operator fun set(name: String, property: FunctionProperty) {
        properties[name] = property
    }
}
