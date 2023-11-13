package com.cjcrafter.openai.files

/**
 * The returned result after attempting to delete a file.
 *
 * @property id The unique id of the file
 * @property deleted Whether the file was deleted
 */
data class FileDeletionStatus(
    val id: String,
    val deleted: Boolean,
)