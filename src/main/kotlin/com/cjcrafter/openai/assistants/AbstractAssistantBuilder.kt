package com.cjcrafter.openai.assistants

import com.cjcrafter.openai.chat.tool.AbstractTool
import com.cjcrafter.openai.chat.tool.Tool
import com.cjcrafter.openai.files.FileObject
import com.cjcrafter.openai.util.OpenAIDslMarker

/**
 * Abstract class for creating/modifying assistants. This class is not
 * meant to be used directly, instead, use the [CreateAssistantRequest.builder]
 * and [ModifyAssistantRequest.builder] methods.
 */
@OpenAIDslMarker
abstract class AbstractAssistantBuilder<T> protected constructor() {
    protected var model: String? = null
    protected var name: String? = null
    protected var description: String? = null
    protected var instructions: String? = null
    protected var tools: MutableList<Tool>? = null
    protected var fileIds: MutableList<String>? = null
    protected var metadata: MutableMap<String, String>? = null

    fun model(model: String) = apply { this.model = model }

    /**
     * The name of the assistant.
     *
     * @param name The name of the assistant
     * @throws IllegalArgumentException if the name is more than 256 characters
     */
    fun name(name: String?) = apply {
        if (name != null && name.length > 256)
            throw IllegalArgumentException("name must be less than 256 characters")
        this.name = name
    }

    /**
     * The description of the assistant. This is typically extremely short,
     * and for the user's benefit rather than for the assistant's benefit.
     *
     * @param description The description of the assistant
     * @throws IllegalArgumentException if the description is more than 512 characters
     */
    fun description(description: String?) = apply {
        if (description != null && description.length > 512)
            throw IllegalArgumentException("description must be less than 512 characters")
        this.description = description
    }

    /**
     * Sets the instructions for the assistant to follow. Instructions
     * are generally a set of "commands" in natural language that the
     * assistant should follow. This option is where all of your prompt
     * engineering goes.
     *
     * @param instructions The instructions for the assistant to follow
     * @throws IllegalArgumentException if the instructions are more than 32768 characters
     */
    fun instructions(instructions: String?) = apply {
        if (instructions != null && instructions.length > 32768)
            throw IllegalArgumentException("instructions must be less than 32768 characters")
        this.instructions = instructions
    }

    /**
     * Sets the tools available to the assistant. When using tools, you
     * should use [instructions] to command the assistant to use the tools.
     * Otherwise, the assistant tends to skip over the tools.
     *
     * In general, you should use [addTool] instead of this method. If you
     * are a Kotlin developer, you should use the extension functions.
     *
     * @param tools The tools available to the assistant
     * @throws IllegalArgumentException if there are more than 128 tools
     */
    fun tools(tools: MutableList<Tool>?) = apply {
        if (tools != null && tools.size > 128)
            throw IllegalArgumentException("cannot have more than 128 tools")
        this.tools = tools
    }

    /**
     * Sets the files available to the assistant for data analysis and
     * retrieval. Make sure that the assistant has at least one of
     * data analysis or retrieval tools.
     *
     * In general, you should use [addFile] instead of this method.
     *
     * @param fileIds The file IDs
     * @throws IllegalArgumentException if there are more than 20 files
     */
    fun fileIds(fileIds: MutableList<String>?) = apply {
        if (fileIds != null && fileIds.size > 20)
            throw IllegalArgumentException("cannot have more than 20 files")
        this.fileIds = fileIds
    }

    /**
     * Sets the metadata map. This metadata is not used by OpenAI, instead,
     * it is data that can be used by **YOU**, the developer, to store
     * information.
     *
     * The metadata map must have 16 or fewer keys, and each key must be
     * less than 64 characters. Each value must be less than 512 characters.
     *
     * @param metadata The metadata map
     * @throws IllegalArgumentException if the map has more than 16 keys, or
     * if any key is more than 64 characters, or if any value is more than
     * 512 characters
     */
    fun metadata(metadata: MutableMap<String, String>?) = apply {
        if (metadata != null) {
            if (metadata.size > 16)
                throw IllegalArgumentException("metadata cannot have more than 16 keys")

            for ((key, value) in metadata) {
                if (key.length > 64)
                    throw IllegalArgumentException("metadata key must be less than 64 characters")
                if (value.length > 512)
                    throw IllegalArgumentException("metadata value must be less than 512 characters")
            }
        }

        this.metadata = metadata
    }

    /**
     * Adds the given tool to the assistant's tools.
     *
     * @throws IllegalStateException if there are already 128 tools
     */
    fun addTool(tool: AbstractTool) = apply {
        if (tools == null) tools = mutableListOf()
        if (tools!!.size > 128)
            throw IllegalStateException("cannot have more than 128 tools")
        tools!!.add(tool.toTool())
    }

    /**
     * Adds the given file to the assistant's files.
     *
     * @throws IllegalStateException if there are already 20 files
     */
    fun addFile(fileId: String) = apply {
        if (fileIds == null) fileIds = mutableListOf()
        if (fileIds!!.size > 20)
            throw IllegalStateException("cannot have more than 20 files")

        fileIds!!.add(fileId)
    }

    /**
     * Adds the given file to the assistant's files.
     *
     * @throws IllegalStateException if there are already 20 files
     */
    fun addFile(file: FileObject) = addFile(file.id)

    abstract fun build(): T
}