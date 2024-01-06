package com.cjcrafter.openai.threads.runs

import org.jetbrains.annotations.Contract

/**
 * Handler used to interact with a [RunStep] objects.
 */
interface RunStepHandler {

    /**
     * The ID of the thread this handler is for.
     */
    val threadId: String

    /**
     * The ID of the run this handler is for.
     */
    val runId: String

    /**
     * Retrieves the updated run step object from the given run step.
     *
     * This method returns a new run step object wrapper. The run step parameter is
     * used only for [RunStep.id]. This method is useful for getting updated
     * information about a run step's status or values.
     *
     * @param runStep The run step to retrieve
     * @return The retrieved run step
     */
    @Contract(pure = true)
    fun retrieve(runStep: RunStep) = retrieve(runStep.id)

    /**
     * Retrieves the run step with the given id.
     *
     * @param id The id of the run step to retrieve
     * @return The retrieved run step
     */
    @Contract(pure = true)
    fun retrieve(id: String): RunStep

    /**
     * Lists the 20 most recent steps of a run.
     *
     * @return The list of steps
     */
    @Contract(pure = true)
    fun list(): ListRunStepsResponse = list(null)

    /**
     * Lists steps from this run.
     *
     * @param request The request to use for listing the steps
     * @return The list of steps
     */
    @Contract(pure = true)
    fun list(request: ListRunStepsRequest?): ListRunStepsResponse
}