package com.cjcrafter.openai.chat.tool

import com.cjcrafter.openai.util.FunctionTag
import com.cjcrafter.openai.util.RegexInternals

/**
 * Represents a function for use by ChatGPT in a chat completion. A function is
 * just like an API endpoint. ChatGPT will call the endpoint with parameters,
 * and expect **you**, the developer, to respond with information.
 *
 * This can be used to get the weather, get stock prices, set values in a
 * database, etc. For more information and examples, check out the
 * [openai cookbook](https://cookbook.openai.com/examples/how_to_call_functions_with_chat_models).
 *
 * @property name The name of the function
 * @property parameters Which parameters can ChatGPT pass to the function
 * @property description A description of the function
 */
data class FunctionTool internal constructor(
    @FunctionTag var name: String,
    var parameters: FunctionParameters,
    var description: String? = null,
) : AbstractTool() {

    init {
        if (!name.matches(RegexInternals.FUNCTION))
            throw IllegalArgumentException("Function name must match ${RegexInternals.FUNCTION}")
    }

    override fun getToolType() = ToolType.FUNCTION

    class Builder internal constructor() {
        @FunctionTag private var name: String? = null
        private var parameters: FunctionParameters? = null
        private var description: String? = null

        fun name(@FunctionTag name: String) = apply { this.name = name }
        fun parameters(parameters: FunctionParameters) = apply { this.parameters = parameters }
        fun description(description: String?) = apply { this.description = description }

        /**
         * Enum parameters are parameters that can only be one of a few values.
         * ChatGPT will be forces to select one of the values you provide here.
         *
         * @param name The name of the parameter
         * @param enum The options that ChatGPT must select from
         * @param required True to force ChatGPT to use this parameter
         */
        @JvmOverloads
        fun addEnumParameter(name: String, enum: MutableList<String>, required: Boolean = false) = apply {
            if (parameters == null) parameters = FunctionParameters()
            parameters!![name] = FunctionProperty("string",  enum=enum)
            if (required)
                parameters!!.require(name)
        }

        /**
         * String parameters are parameters that can be any string.
         *
         * @param name The name of the parameter
         * @param description The description of the parameter
         * @param required True to force ChatGPT to use this parameter
         */
        @JvmOverloads
        fun addStringParameter(name: String, description: String?, required: Boolean = false) = addParameter(name, "string", description, required)

        /**
         * Integer parameters are parameters that can be any integer.
         *
         * @param name The name of the parameter
         * @param description The description of the parameter
         * @param required True to force ChatGPT to use this parameter
         */
        @JvmOverloads
        fun addIntegerParameter(name: String, description: String?, required: Boolean = false) = addParameter(name, "integer", description, required)

        /**
         * For any other type of parameter, use this method. In general, there
         * is no strict list of types for you to choose from. Instead, ChatGPT
         * will read the string and interpret what the type should be.
         *
         * @param name The name of the parameter
         * @param type The type of the parameter
         * @param description The description of the parameter
         * @param required True to force ChatGPT to use this parameter
         */
        @JvmOverloads
        fun addParameter(name: String, type: String, description: String?, required: Boolean = false) = apply {
            if (parameters == null) parameters = FunctionParameters()
            parameters!![name] = FunctionProperty(type, description)
            if (required)
                parameters!!.require(name)
        }

        fun build() = FunctionTool(
            name = name ?: throw IllegalStateException("Name must be set"),
            parameters = parameters ?: throw IllegalStateException("Parameters must be set"),
            description = description,
        )
    }

    companion object {
        fun builder() = Builder()
    }
}