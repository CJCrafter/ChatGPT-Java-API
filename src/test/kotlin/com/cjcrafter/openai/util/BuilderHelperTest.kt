package com.cjcrafter.openai.util

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class BuilderHelperTest {

    @Test
    fun `test key too long`() {
        assertThrows(IllegalArgumentException::class.java) {
            BuilderHelper.assertMetadata("a".repeat(BuilderHelper.MAX_METADATA_KEY_LENGTH + 1), "a")
        }
    }

    @Test
    fun `test value too long`() {
        assertThrows(IllegalArgumentException::class.java) {
            BuilderHelper.assertMetadata("a", "a".repeat(BuilderHelper.MAX_METADATA_VALUE_LENGTH + 1))
        }
    }

    @Test
    fun `test map too big`() {
        val metadata = buildMap {
            for (i in 0..BuilderHelper.MAX_METADATA_SIZE)
                put("$i", "a")
        }
        assertThrows(IllegalArgumentException::class.java) {
            BuilderHelper.assertMetadata(metadata)
        }
    }

    @Test
    fun `test key too long in map`() {
        val metadata = mapOf("a".repeat(BuilderHelper.MAX_METADATA_KEY_LENGTH + 1) to "a")
        assertThrows(IllegalArgumentException::class.java) {
            BuilderHelper.assertMetadata(metadata)
        }
    }

    @Test
    fun `test value too long in map`() {
        val metadata = mapOf("a" to "a".repeat(BuilderHelper.MAX_METADATA_VALUE_LENGTH + 1))
        assertThrows(IllegalArgumentException::class.java) {
            BuilderHelper.assertMetadata(metadata)
        }
    }

    @Test
    fun `test map already full`() {
        val metadata = buildMap {
            for (i in 0 until BuilderHelper.MAX_METADATA_SIZE)
                put("$i", "a")
        }
        assertThrows(IllegalArgumentException::class.java) {
            BuilderHelper.tryAddMetadata(metadata)
        }
    }
}