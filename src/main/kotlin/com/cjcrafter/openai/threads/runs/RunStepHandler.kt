package com.cjcrafter.openai.threads.runs

/**
 * Handler class for all [RunStep] operations.
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
     */
    fun retrieve(runStep: RunStep) = retrieve(runStep.id)

    /**
     * Retrieves the run step with the given id.
     */
    fun retrieve(id: String): RunStep

    /**
     * Lists the first 20 steps of a run.
     */
    fun list(): ListRunStepsResponse = list(null)

    /**
     * Lists steps from this run.
     */
    fun list(request: ListRunStepsRequest?): ListRunStepsResponse
}