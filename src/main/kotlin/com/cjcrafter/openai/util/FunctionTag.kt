package com.cjcrafter.openai.util

import org.intellij.lang.annotations.Pattern

/**
 * This is used to ensure function names follow OpenAI API requirements.
 * Uses **`^[a-zA-Z0-9_-]{1,64}$`**
 */
@MustBeDocumented
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.BINARY)
@Pattern(RegexInternals.FUNCTION_PATTERN)
annotation class FunctionTag
