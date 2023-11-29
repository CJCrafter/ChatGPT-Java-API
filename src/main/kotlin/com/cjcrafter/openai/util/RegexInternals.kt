package com.cjcrafter.openai.util

object RegexInternals {
    const val FUNCTION_PATTERN = """^[a-zA-Z0-9_-]{1,64}$"""
    val FUNCTION = Regex(FUNCTION_PATTERN)
}
