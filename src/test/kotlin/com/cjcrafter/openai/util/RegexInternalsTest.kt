package com.cjcrafter.openai.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class RegexInternalsTest {

    @ParameterizedTest
    @ValueSource(strings = ["a", "A", "0", "_", "-", "aA0_-"])
    fun `test valid function names`(name: String) {
        assertTrue(RegexInternals.FUNCTION.matches(name))
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "\$hello", "#hello", "hello\$", "hello#", "hello\$world", "hello#world"])
    fun `test invalid function names`(name: String) {
        assertFalse(RegexInternals.FUNCTION.matches(name))
    }
}