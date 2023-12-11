package com.cjcrafter.openai.threads.runs

interface RunHandler {

    /**
     * The ID of the thread this handler is for.
     */
    val threadId: String

    /**
     * Creates (and starts) a new [Run] object.
     */
    fun create(request: CreateRunRequest): Run

    /**
     * Retrieves the updated run object from the given run.
     *
     * This method returns a new run object wrapper. The run parameter is
     * used only for [Run.id]. This method is useful for getting updated
     * information about a run's status or values.
     */
    fun retrieve(run: Run): Run = retrieve(run.id)

    /**
     * Retrieves the run with the given id.
     */
    fun retrieve(id: String): Run

    /**
     * Modifies the given run to have the given updated values.
     */
    fun modify(run: Run, request: ModifyRunRequest): Run = modify(run.id, request)

    /**
     * Modifies the run with the given id to have the given updated values.
     */
    fun modify(id: String, request: ModifyRunRequest): Run

    /**
     * Lists the first 100 runs for the thread.
     */
    fun list(): ListRunsResponse = list(null)

    /**
     * Lists runs for the thread.
     */
    fun list(request: ListRunsRequest?): ListRunsResponse

    /**
     * When a run has the status [RunStatus.REQUIRED_ACTION] and the action
     * type is [RequiredAction.Type.SUBMIT_TOOL_CALLS], this method can be used
     * to submit the outputs from the tool calls once they're all completed.
     * All outputs must be submitted in a single request.
     */
    fun submitToolOutputs(run: Run, submission: SubmitToolOutputs): Run = submitToolOutputs(run.id, submission)

    /**
     * When a run has the status [RunStatus.REQUIRED_ACTION] and the action
     * type is [RequiredAction.Type.SUBMIT_TOOL_CALLS], this method can be used
     * to submit the outputs from the tool calls once they're all completed.
     * All outputs must be submitted in a single request.
     */
    fun submitToolOutputs(id: String, submission: SubmitToolOutputs): Run

    /**
     * Cancels the given run that is [RunStatus.IN_PROGRESS].
     */
    fun cancel(run: Run): Run = cancel(run.id)

    /**
     * Cancels the run with the given id that is [RunStatus.IN_PROGRESS].
     */
    fun cancel(id: String): Run

    fun steps(run: Run): RunStepHandler = steps(run.id)

    fun steps(id: String): RunStepHandler
}