package com.cjcrafter.openai.files

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Represents the reason a file was uploaded to the OpenAI API.
 */
enum class FilePurpose {

    /**
     * A file used to create/validate a fine-tuned model. When uploading a file
     * for this purpose, the uploaded file must be a `.jsonl` (JSON list) file,
     * where each line of the file is a separate training example. No other file
     * types are accepted.
     */
    @JsonProperty("fine-tune")
    FINE_TUNE,

    /**
     * Files resulting from a fine-tuning task. This is used internally by OpenAI,
     * If you try to upload a file with this purpose, you will get an error.
     */
    @JsonProperty("fine-tune-results")
    FINE_TUNE_RESULTS,

    /**
     * A file that an assistant can use for data analysis or information retrieval.
     */
    @JsonProperty("assistants")
    ASSISTANTS,

    /**
     * A file output from an assistant. This is used internally by OpenAI, if
     * you try to upload a file with this purpose, you will get an error.
     */
    @JsonProperty("assistants_output")
    ASSISTANTS_OUTPUT
}