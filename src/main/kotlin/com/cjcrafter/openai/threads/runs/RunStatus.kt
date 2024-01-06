package com.cjcrafter.openai.threads.runs

import com.cjcrafter.openai.assistants.Assistant
import com.cjcrafter.openai.chat.tool.Tool
import com.cjcrafter.openai.threads.Thread
import com.cjcrafter.openai.threads.message.ThreadMessage
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the current state of a [Run]. You can view the current status of
 * a run by looking at the [Run.status] field.
 *
 * @property isTerminal Whether the OpenAI API is "done," and expecting you to
 *                      take some action.
 */
enum class RunStatus(
    val isTerminal: Boolean,
) {

    /**
     * When [Run]s are first created or when you complete the [RequiredAction],
     * they are moved to this 'queued' status. They should _almost immediately_
     * move to [IN_PROGRESS].
     */
    @JsonProperty("queued")
    QUEUED(false),

    /**
     * While [IN_PROGRESS], the [Assistant] uses the mode and tools to perform
     * steps. You can view progress being made by the [Run] by examining the
     * [RunStep]s.
     */
    @JsonProperty("in_progress")
    IN_PROGRESS(false),

    /**
     * When using the [Tool.FunctionTool], the [Run] will move to a [REQUIRED_ACTION]
     * state once the model determines the names and arguments of the functions
     * to be called. You must then run those functions and submit the outputs
     * using [RunHandler.submitToolOutputs] before the run proceeds. If the
     * outputs are not provided before the [Run.expiresAt] timestamp passes
     * (roughly 10 minutes past creation), the run will move to an [EXPIRED]
     * status.
     */
    @JsonProperty("required_action")
    REQUIRED_ACTION(true),

    /**
     * You can attempt to cancel an [IN_PROGRESS] [Run] using [RunHandler.cancel].
     * Once the attempt to cancel succeeds, status of the [Run] moves to
     * [CANCELLED]. Cancellation is attempted but not guarenteed.
     */
    @JsonProperty("cancelling")
    CANCELLING(false),

    /**
     * The [Run] was successfully cancelled.
     */
    @JsonProperty("cancelled")
    CANCELLED(true),

    /**
     * You can view the reason for the failure by looking at the [Run.lastError]
     * object in the run (see [RunError]). The timestamp for the failure is
     * recorded under the [Run.failedAt].
     */
    @JsonProperty("failed")
    FAILED(true),

    /**
     * The [Run] successfully completed! You can now view all [ThreadMessage]s
     * the [Assistant] added to the [Thread], and all the steps the [Run] took.
     * You can also continue the conversation by adding more [ThreadMessage]s
     * to the [Thread] and creating another [Run].
     */
    @JsonProperty("completed")
    COMPLETED(true),

    /**
     * This happens when the function calling outputs were not submitted before
     * [Run.expiresAt] and the [Run] expires. Additionally, if the runs take
     * too long to execute and go beyond the time stated in [Run.expiresAt],
     * OpenAI's systems will expire the [Run].
     */
    @JsonProperty("expired")
    EXPIRED(true),
}