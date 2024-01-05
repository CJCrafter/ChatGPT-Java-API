package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.assistants.Assistant
import com.cjcrafter.openai.chat.tool.Tool
import com.cjcrafter.openai.threads.runs.RunStatus.*
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * Each [Run] is broken down into steps. It is common for a [Run] to only have
 * 1 step; creating a message. But with tool calls involved, an [Assistant] can
 * create an arbitrary number of steps.
 *
 * @property id The unique id of this step, which can be used in [RunStepHandler.retrieve].
 * Always starts with 'step_'. This is different from [Run.id], which is also stored in this class as [runId]
 * @property createdAt The unix timestamp of when this step was created
 * @property assistantId The ID of the [Assistant] that created this step
 * @property threadId The ID of the [Thread] that this step was created in
 * @property runId The ID of the [Run] that this step was created in
 * @property type The type of data stored by this step. This will match [Details.type] stored in [stepDetails]
 * @property status The status of this step (Can be either [IN_PROGRESS], [CANCELLED], [FAILED], [COMPLETED], or [EXPIRED])
 * @property stepDetails The data generated at this step. The type of this data is determined by [type].
 * Make sure to check the type of this data before using it.
 * @property lastError The last error associated with this run step. Will be `null` if there are no errors.
 * @property expiredAt The unix timestamp of when this step expired. This is only present if the parent run has expired.
 * @property cancelledAt The unix timestamp for when the step was cancelled.
 * @property failedAt The unix timestamp for when the step failed.
 * @property completedAt The unix timestamp for when the step completed.
 * @property metadata Metadata for this step. This can be useful for storing additional information about the object.
 */
data class RunStep(
    @JsonProperty(required = true) val id: String,
    @JsonProperty("created_at", required = true) val createdAt: Int,
    @JsonProperty("assistant_id", required = true) val assistantId: String,
    @JsonProperty("thread_id", required = true) val threadId: String,
    @JsonProperty("run_id", required = true) val runId: String,
    @JsonProperty(required = true) val type: Type,
    @JsonProperty(required = true) val status: RunStatus,
    @JsonProperty("step_details", required = true) val stepDetails: Details,
    @JsonProperty("last_error") val lastError: RunError?,
    @JsonProperty("expired_at") val expiredAt: Int?,
    @JsonProperty("cancelled_at") val cancelledAt: Int?,
    @JsonProperty("failed_at") val failedAt: Int?,
    @JsonProperty("completed_at") val completedAt: Int?,
    @JsonProperty val metadata: Map<String, String> = emptyMap(),
) {

    /**
     * An enum that holds all possible types of steps.
     */
    enum class Type {

        /**
         * When this step of the run created a message.
         */
        @JsonProperty("message_creation")
        MESSAGE_CREATION,

        /**
         * When this step of the run created a tool call. This is used for all
         * tool call types, not just [Tool.Type.FUNCTION].
         */
        @JsonProperty("tool_calls")
        TOOL_CALLS,
    }

    /**
     * A sealed class that represents the details of a step.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(MessageCreationDetails::class, name = "message_creation"),
        JsonSubTypes.Type(ToolCallsDetails::class, name = "tool_calls"),
    )
    sealed class Details {

        /**
         * The type of data stored by this step. This will match [RunStep.type].
         */
        abstract val type: Type
    }
}