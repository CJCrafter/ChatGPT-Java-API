package com.cjcrafter.openai.files

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents a response from the [list files](https://platform.openai.com/docs/api-reference/files)
 * endpoint.
 *
 * @property hasMore Whether there are more files to retrieve from the API.
 * @property data The list of files.
 */
data class ListFilesResponse(
    @JsonProperty("has_more", required = true) val hasMore: Boolean,
    @JsonProperty(required = true) val data: List<FileObject>,
)