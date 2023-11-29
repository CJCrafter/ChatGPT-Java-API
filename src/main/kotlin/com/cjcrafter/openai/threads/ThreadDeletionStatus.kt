package com.cjcrafter.openai.threads

/**
 * Represents the status of a thread deletion request.
 *
 * @property id The id of the thread that was deleted
 * @property deleted Whether the thread was deleted
 */
data class ThreadDeletionStatus(
    val id: String,
    val deleted: Boolean,
)
