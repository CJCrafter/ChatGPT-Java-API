package com.cjcrafter.openai.threads.runs

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * A data class which represents an error in a [Run].
 *
 * @property code The reason a run failed
 * @property message A human-readable description of the error
 */
data class RunError(
    val code: ErrorCode,
    val message: String,
) {
    enum class ErrorCode {

        @JsonProperty("server_error")
        SERVER_ERROR,

        @JsonProperty("rate_limit_exceeded")
        RATE_LIMIT_EXCEEDED,
    }
}