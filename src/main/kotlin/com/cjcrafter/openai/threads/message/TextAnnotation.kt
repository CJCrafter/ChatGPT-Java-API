package com.cjcrafter.openai.threads.message

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(TextAnnotation.FileCitation::class, name = "file_citation"),
    JsonSubTypes.Type(TextAnnotation.FilePath::class, name = "file_path"),
)
sealed class TextAnnotation {

    data class FileCitation(
        @JsonProperty(required = true) val text: String,
        @JsonProperty("file_citation", required = true) val fileCitation: FileQuote,
        @JsonProperty("start_index", required = true) val startIndex: Int,
        @JsonProperty("end_index", required = true) val endIndex: Int,
    ): TextAnnotation() {

        data class FileQuote(
            @JsonProperty("file_id", required = true) val fileId: String,
            @JsonProperty(required = true) val quote: String,
        )
    }

    data class FilePath(
        @JsonProperty(required = true) val text: String,
        @JsonProperty("file_citation", required = true) val filePath: FileWrapper,
        @JsonProperty("start_index", required = true) val startIndex: Int,
        @JsonProperty("end_index", required = true) val endIndex: Int,
    ): TextAnnotation() {

        data class FileWrapper(
            @JsonProperty("file_id", required = true) val fileId: String,
        )
    }
}