package com.cjcrafter.openai.assistants

import org.jetbrains.annotations.Contract

/**
 * Represents the handler for the assistants endpoint. This class holds all the
 * actions that can be performed on an assistant.
 */
interface AssistantHandler {

    /**
     * Creates a new assistant with the given properties. This method will
     * return the data associated with the assistant.
     *
     * @param request The request to create the assistant
     * @return The created assistant
     */
    fun create(request: CreateAssistantRequest): Assistant

    /**
     * Retrieves the assistant with the given id. This method will return a new
     * instance of the assistant, and will not modify the given instance.
     *
     * This method is useful for getting the latest data for an assistant
     * (Though, in general, you should be using the return value of the
     * [modify] method instead of making additional API calls).
     *
     * @param assistant The assistant to retrieve
     * @return The new instance of the retrieved assistant
     */
    @Contract(pure = true)
    fun retrieve(assistant: Assistant): Assistant = retrieve(assistant.id)

    /**
     * Retrieve the assistant with the given id.
     *
     * @param id The id of the assistant to retrieve
     * @return The assistant with the given id
     */
    @Contract(pure = true)
    fun retrieve(id: String): Assistant

    /**
     * Attempts to delete the given assistant. To confirm deletion, you should
     * check [AssistantDeletionStatus.deleted].
     *
     * @param assistant The assistant to delete
     * @return The deletion status of the assistant
     */
    fun delete(assistant: Assistant): AssistantDeletionStatus = delete(assistant.id)

    /**
     * Attempts to delete the assistant with the given id. To confirm deletion,
     * you should check [AssistantDeletionStatus.deleted].
     *
     * @param id The id of the assistant to delete
     * @return The deletion status of the assistant
     */
    fun delete(id: String): AssistantDeletionStatus

    /**
     * Lists the 20 most recent assistants.
     *
     * @return The list of assistants
     */
    @Contract(pure = true)
    fun list(): ListAssistantResponse = list(null)

    /**
     * Lists assistants with the given query parameters.
     *
     * @param request The query parameters
     * @return The list of assistants
     */
    @Contract(pure = true)
    fun list(request: ListAssistantRequest?): ListAssistantResponse // Cannot use @JvmOverloads in interfaces

    /**
     * Shorthand to modify the given assistant. Note that this method will
     * return a new instance of the assistant, and will not modify the given
     * instance.
     *
     * @param assistant The assistant to modify
     * @param request The request to modify the assistant
     */
    fun modify(assistant: Assistant, request: ModifyAssistantRequest): Assistant = modify(assistant.id, request)

    /**
     * Modifies the given assistant. For any nonnull fields in the given
     * request, the corresponding field in the assistant will be overwritten.
     *
     * If you want to append to the corresponding field, you must first retrieve
     * the assistant, modify the field, and then call this method.
     *
     * @param request The request to modify the assistant
     * @return The modified assistant
     */
    fun modify(id: String, request: ModifyAssistantRequest): Assistant
}