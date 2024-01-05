package com.cjcrafter.openai.chat.tool

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

/**
 * Represents the data for a [Tool.Type.CODE_INTERPRETER] tool call. This
 * contains the code that was run by the code interpreter, and the outputs
 * of the code interpreter.
 *
 * @property input The code that was run by the code interpreter
 * @property outputs The data output by the code interpreter
 */
data class CodeInterpreter(
    @JsonProperty(required = true) val input: String,
    @JsonProperty(required = true) val outputs: List<Output>
) {

    /**
     * A sealed class that represents the output of 1 [CodeInterpreter] step.
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes(
        JsonSubTypes.Type(LogOutput::class, name = "logs"),
        JsonSubTypes.Type(ImageOutput::class, name = "image"),
    )
    sealed class Output {

        /**
         * The type of the output.
         */
        abstract val type: Type

        enum class Type {

            /**
             * When the code interpreter outputs text.
             */
            @JsonProperty("logs")
            LOGS,

            /**
             * When the code interpreter outputs an image file.
             */
            @JsonProperty("image")
            IMAGE,
        }
    }

    /**
     * Represents an [Output.Type.LOGS] output. This only holds the text that
     * was outputted by the code interpreter.
     *
     * @property text The text output
     */
    data class LogOutput(
        @JsonProperty(required = true) val logs: String,
    ) : Output() {
        override val type: Type = Type.LOGS
    }

    /**
     * Represents an [Output.Type.IMAGE] output. This only holds the ID of the
     * generated image file. You can use this ID to retrieve the image file.
     *
     * @property image The image output
     * @see com.cjcrafter.openai.files.FileHandler.retrieve
     */
    data class ImageOutput(
        val image: Image,
    ) : Output() {
        override val type: Type = Type.IMAGE

        /**
         * Holds data about the image output by the code interpreter.
         *
         * @property fileId The unique ID of the output file, which can be used to retrieve the file
         */
        data class Image(
            @JsonProperty("file_id", required = true) val fileId: String,
        )
    }
}