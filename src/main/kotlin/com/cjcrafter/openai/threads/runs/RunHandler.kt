package com.cjcrafter.openai.threads.runs

import org.jetbrains.annotations.Contract

/**
 * Handler used to interact with a [Run] objects.
 */
interface RunHandler {

    /**
     * The ID of the thread this handler is for.
     */
    val threadId: String

    /**
     * Creates (and starts) a new [Run] object.
     *
     * @param request The values of the run to create
     * @return The created run
     */
    fun create(request: CreateRunRequest): Run

    // TODO
    fun stream(request: CreateRunRequest): Any

    /**
     * Retrieves the updated run object from the given run.
     *
     * This method returns a new run object wrapper. The run parameter is
     * used only for [Run.id]. This method is useful for getting updated
     * information about a run's status or values.
     *
     * @param run The run to retrieve
     * @return The retrieved run
     */
    @Contract(pure = true)
    fun retrieve(run: Run): Run = retrieve(run.id)

    /**
     * Retrieves the run with the given id.
     *
     * @param id The id of the run to retrieve
     * @return The retrieved run
     */
    @Contract(pure = true)
    fun retrieve(id: String): Run

    /**
     * Modifies the given run to have the given updated values.
     *
     * @param run The run to modify
     * @param request The values to update the run with
     */
    fun modify(run: Run, request: ModifyRunRequest): Run = modify(run.id, request)

    /**
     * Modifies the run with the given id to have the given updated values.
     *
     * @param id The id of the run to modify
     * @param request The values to update the run with
     */
    fun modify(id: String, request: ModifyRunRequest): Run

    /**
     * Lists the 20 most recent runs for the thread.
     *
     * @return The list of runs
     */
    @Contract(pure = true)
    fun list(): ListRunsResponse = list(null)

    /**
     * Lists runs for the thread.
     *
     * @param request The values to filter the runs by
     * @return The list of runs
     */
    @Contract(pure = true)
    fun list(request: ListRunsRequest?): ListRunsResponse

    /**
     * When a run has the status [RunStatus.REQUIRED_ACTION] and the action
     * type is [RequiredAction.Type.SUBMIT_TOOL_CALLS], this method can be used
     * to submit the outputs from the tool calls once they're all completed.
     * All outputs must be submitted in a single request.
     *
     * @param run The run to submit the outputs for
     * @param submission The tool outputs
     * @return The updated run
     */
    fun submitToolOutputs(run: Run, submission: SubmitToolOutputs): Run = submitToolOutputs(run.id, submission)

    /**
     * When a run has the status [RunStatus.REQUIRED_ACTION] and the action
     * type is [RequiredAction.Type.SUBMIT_TOOL_CALLS], this method can be used
     * to submit the outputs from the tool calls once they're all completed.
     * All outputs must be submitted in a single request.
     *
     * @param id The id of the run to submit the outputs for
     * @param submission The tool outputs
     * @return The updated run
     */
    fun submitToolOutputs(id: String, submission: SubmitToolOutputs): Run

    /**
     * Cancels the given run that is [RunStatus.IN_PROGRESS].
     *
     * @param run The run to cancel
     * @return The updated run
     */
    fun cancel(run: Run): Run = cancel(run.id)

    /**
     * Cancels the run with the given id that is [RunStatus.IN_PROGRESS].
     *
     * @param id The id of the run to cancel
     * @return The updated run
     */
    fun cancel(id: String): Run

    /**
     * Returns a handler for interacting with the steps in the given run.
     *
     * @param run The run to get the steps for
     * @return The steps handler
     */
    @Contract(pure = true)
    fun steps(run: Run): RunStepHandler = steps(run.id)

    /**
     * Returns a handler for interacting with the steps in the run with the
     * given id.
     *
     * @param id The id of the run to get the steps for
     * @return The steps handler
     */
    @Contract(pure = true)
    fun steps(id: String): RunStepHandler
}