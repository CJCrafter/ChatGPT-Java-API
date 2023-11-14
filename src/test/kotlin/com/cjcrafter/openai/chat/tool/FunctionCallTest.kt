package com.cjcrafter.openai.chat.tool

import com.cjcrafter.openai.OpenAI
import com.cjcrafter.openai.exception.HallucinationException
import com.fasterxml.jackson.module.kotlin.readValue
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class FunctionCallTest {

    @Test
    fun `test bad enum`() {
        val tools = listOf(
            functionTool {
                name("enum_checker")
                description("This function is used to test the enum parameter")
                addEnumParameter("enum", mutableListOf("a", "b", "c"))
            }.toTool()
        )
        @Language("json")
        val json = "{\"name\": \"enum_checker\", \"arguments\": \"{\\\"enum\\\": \\\"d\\\"}\"}" // d is not a valid enum
        val call = FunctionCall("enum_checker", json)

        assertThrows<HallucinationException> {
            call.tryParseArguments(tools)
        }
    }

    @Test
    fun `test good enum`() {
        val tools = listOf(
            functionTool {
                name("enum_checker")
                description("This function is used to test the enum parameter")
                addEnumParameter("enum", mutableListOf("a", "b", "c"))
            }.toTool()
        )
        @Language("json")
        val json = "{\"name\": \"enum_checker\", \"arguments\": \"{\\\"enum\\\": \\\"a\\\"}\"}" // a is a valid enum
        val call = OpenAI.createObjectMapper().readValue<FunctionCall>(json)

        val args = call.tryParseArguments(tools)
        assertTrue(args.contains("enum")) { "enum should be present in the arguments" }
        assertEquals("a", args["enum"]?.asText())
    }

    @Test
    fun `test bad integer`() {
        val tools = listOf(
            functionTool {
                name("integer_checker")
                description("This function is used to test the integer parameter")
                addIntegerParameter("integer", "test parameter")
            }.toTool()
        )
        @Language("json")
        val json = "{\"name\": \"integer_checker\", \"arguments\": \"{\\\"integer\\\": \\\"not an integer\\\"}\"}" // not an integer
        val call = OpenAI.createObjectMapper().readValue<FunctionCall>(json)

        assertThrows<HallucinationException> {
            call.tryParseArguments(tools)
        }
    }

    @Test
    fun `test good integer`() {
        val tools = listOf(
            functionTool {
                name("integer_checker")
                description("This function is used to test the integer parameter")
                addIntegerParameter("integer", "test parameter")
            }.toTool()
        )
        @Language("json")
        val json = "{\"name\": \"integer_checker\", \"arguments\": \"{\\\"integer\\\": 1}\"}" // 1 is an integer
        val call = OpenAI.createObjectMapper().readValue<FunctionCall>(json)

        val args = call.tryParseArguments(tools)
        assertTrue(args.contains("integer")) { "integer should be present in the arguments" }
        assertEquals(1, args["integer"]?.asInt())
    }

    @Test
    fun `test bad boolean`() {
        val tools = listOf(
            functionTool {
                name("boolean_checker")
                description("This function is used to test the boolean parameter")
                addBooleanParameter("is_true", "test parameter")
            }.toTool()
        )
        @Language("json")
        val json = "{\"name\": \"boolean_checker\", \"arguments\": \"{\\\"boolean\\\": \\\"not a boolean\\\"}\"}" // not a boolean
        val call = OpenAI.createObjectMapper().readValue<FunctionCall>(json)

        assertThrows<HallucinationException> {
            call.tryParseArguments(tools)
        }
    }

    @Test
    fun `test good boolean`() {
        val tools = listOf(
            functionTool {
                name("boolean_checker")
                description("This function is used to test the boolean parameter")
                addBooleanParameter("is_true", "test parameter")
            }.toTool()
        )
        @Language("json")
        val json = "{\"name\": \"boolean_checker\", \"arguments\": \"{\\\"is_true\\\": true}\"}" // true is a boolean
        val call = OpenAI.createObjectMapper().readValue<FunctionCall>(json)

        val args = call.tryParseArguments(tools)
        assertTrue(args.contains("is_true")) { "is_true should be present in the arguments" }
        assertTrue(args["is_true"]?.asBoolean() ?: false)
    }

    @Test
    fun `test missing required parameters`() {
        val tools = listOf(
            functionTool {
                name("required_parameter_function")
                description("This function is used to test the required parameter")
                addIntegerParameter("required", "test parameter", required = true)
                addBooleanParameter("not_required", "test parameter")
            }.toTool()
        )
        @Language("json")
        val json = "{\"name\": \"required_parameter_function\", \"arguments\": \"{\\\"not_required\\\": true}\"}" // missing required parameter
        val call = OpenAI.createObjectMapper().readValue<FunctionCall>(json)

        assertThrows<HallucinationException> {
            call.tryParseArguments(tools)
        }
    }

    @Test
    fun `test has required parameter`() {
        val tools = listOf(
            functionTool {
                name("required_parameter_function")
                description("This function is used to test the required parameter")
                addIntegerParameter("required", "test parameter", required = true)
                addBooleanParameter("not_required", "test parameter")
            }.toTool()
        )
        @Language("json")
        val json = "{\"name\": \"required_parameter_function\", \"arguments\": \"{\\\"required\\\": 1, \\\"not_required\\\": true}\"}" // has required parameter
        val call = OpenAI.createObjectMapper().readValue<FunctionCall>(json)

        assertDoesNotThrow("Should not throw an exception when the required parameter is present") {
            call.tryParseArguments(tools)
        }
    }

    @Test
    fun `test invalid function name`() {
        val tools = listOf(
            functionTool {
                name("function_name_checker")
                description("This function is used to test the function name")
                noParameters()
            }.toTool()
        )
        @Language("json")
        val json = "{\"name\": \"invalid_function_name\", \"arguments\": \"{}\"}" // invalid function name
        val call = OpenAI.createObjectMapper().readValue<FunctionCall>(json)

        assertThrows<HallucinationException> {
            call.tryParseArguments(tools)
        }
    }
}