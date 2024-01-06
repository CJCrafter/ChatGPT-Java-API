package com.cjcrafter.openai.files

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * The returned result after attempting to delete a file.
 *
 * @property id The unique id of the file
 * @property deleted Whether the file was deleted
 */
data class FileDeletionStatus(
    @JsonProperty(required = true) val id: String,
    @JsonProperty(required = true) val deleted: Boolean,
)