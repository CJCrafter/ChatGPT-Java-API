package com.cjcrafter.openai.exception

import org.intellij.lang.annotations.Language

class HallucinationException(
    message: String,
) : Exception(message) {

    /**
     * Returns the JSON representation of this error. This can be used to send
     * the error back to ChatGPT, so it is aware it made an error and can try
     * to correct it (or continue on without the tool).
     */
    fun jsonDumps(): String {
        @Language("JSON")
        val json = """"error":{"message":"$message"}"""
        return json
    }
}