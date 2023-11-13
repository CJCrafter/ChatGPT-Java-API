package com.cjcrafter.openai.files

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a response from the [list files](https://platform.openai.com/docs/api-reference/files)
 * endpoint.
 *
 * @property data The list of files.
 */
data class ListFilesResponse(
    @JsonProperty("has_more") val hasMore: Boolean,
    val data: List<FileObject>,
)//: List<FileObject> by data