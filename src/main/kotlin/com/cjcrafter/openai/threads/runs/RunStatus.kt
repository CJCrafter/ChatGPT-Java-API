package com.cjcrafter.openai.threads.runs

import com.fasterxml.jackson.annotation.JsonProperty

enum class RunStatus(
    val isDone: Boolean,
) {

    @JsonProperty("queued")
    QUEUED(false),

    @JsonProperty("in_progress")
    IN_PROGRESS(false),

    @JsonProperty("requires_action")
    REQUIRES_ACTION(true),

    @JsonProperty("cancelling")
    CANCELLING(false),

    @JsonProperty("cancelled")
    CANCELLED(true),

    @JsonProperty("failed")
    FAILED(true),

    @JsonProperty("completed")
    COMPLETED(true),

    @JsonProperty("expired")
    EXPIRED(true),
}