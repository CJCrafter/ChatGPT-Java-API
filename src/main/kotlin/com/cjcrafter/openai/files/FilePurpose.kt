package com.cjcrafter.openai.files

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the reason a file was uploaded to the OpenAI API.
 */
enum class FilePurpose {

    /**
     * A file used to create/validate a fine-tuned model.
     */
    @JsonProperty("fine-tune")
    FINE_TUNE,

    /**
     * Files resulting from a fine-tuning task.
     */
    @JsonProperty("fine-tune-results")
    FINE_TUNE_RESULTS,

    /**
     * A file that an assistant can use for data analysis or information retrieval.
     */
    @JsonProperty("assistants")
    ASSISTANTS,

    /**
     * A file output from an assistant.
     */
    @JsonProperty("assistants_output")
    ASSISTANTS_OUTPUT
}