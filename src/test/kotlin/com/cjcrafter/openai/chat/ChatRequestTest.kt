package com.cjcrafter.openai.chat

import com.cjcrafter.openai.OpenAI
import com.cjcrafter.openai.chat.ChatMessage.Companion.toSystemMessage
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ChatRequestTest {

    @ParameterizedTest
    @MethodSource("provide_serialize")
    fun `test deserialize to json`(obj: Any, json: String) {
        val objectMapper = OpenAI.createObjectMapper()
        val expected = objectMapper.readTree(json)
        val actual = objectMapper.readTree(objectMapper.writeValueAsString(obj))
        assertEquals(expected, actual)
    }

    @ParameterizedTest
    @MethodSource("provide_serialize")
    fun `test serialize from json`(expected: Any, json: String) {
        val objectMapper = OpenAI.createObjectMapper()
        val actual = objectMapper.readValue(json, expected::class.java)
        assertEquals(expected, actual)
    }

    companion object {
        @JvmStatic
        fun provide_serialize(): Stream<Arguments> {
            return buildList<Arguments> {

                @Language("JSON")
                var json = """
                    {
                        "messages": [
                            {
                                "role": "system",
                                "content": "Be as helpful as possible"
                            }
                        ],
                        "model": "gpt-3.5-turbo"
                    }
                """.trimIndent()
                add(Arguments.of(
                    ChatRequest.builder()
                        .model("gpt-3.5-turbo")
                        .messages(mutableListOf("Be as helpful as possible".toSystemMessage()))
                        .build(),
                    json
                ))

                json = """
                    {
                        "messages": [
                            {
                                "role": "system",
                                "content": "Be as helpful as possible"
                            },
                            {
                                "role": "user",
                                "content": "What is 2 + 2?"
                            }
                        ],
                        "model": "gpt-3.5-turbo",
                        "tools": [
                            {
                                "type": "function",
                                "function": {
                                    "name": "solve_math_problem",
                                    "parameters": {
                                        "type": "object",
                                        "properties": {
                                            "equation": {
                                                "type": "string",
                                                "description": "The math problem for you to solve"
                                            }
                                        },
                                        "required": [
                                            "equation"
                                        ]
                                    },
                                    "description": "Returns the result of a math problem as a double"
                                }
                            }
                        ]
                    }
                """.trimIndent()
                add(Arguments.of(
                    chatRequest {
                        model("gpt-3.5-turbo")
                        messages(mutableListOf(
                            ChatMessage(ChatUser.SYSTEM, "Be as helpful as possible"),
                            ChatMessage(ChatUser.USER, "What is 2 + 2?")
                        ))
                        function {
                            name("solve_math_problem")
                            description("Returns the result of a math problem as a double")
                            addStringParameter("equation", "The math problem for you to solve", true)
                        }
                    },
                    json
                ))

            }.stream()
        }
    }
}
