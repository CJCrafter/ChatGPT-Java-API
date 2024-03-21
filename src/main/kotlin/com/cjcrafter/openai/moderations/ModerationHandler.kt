package com.cjcrafter.openai.moderations

/**
 * Handler used to interact with [Moderation] objects.
 */
interface ModerationHandler {

    /**
     * Creates a new moderation request with the given options.
     *
     * @param request The values of the moderation to create
     * @return The created moderation
     */
    fun create(request: CreateModerationRequest): Moderation
}