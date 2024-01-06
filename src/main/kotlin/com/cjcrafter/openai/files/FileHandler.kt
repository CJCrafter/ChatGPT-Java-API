package com.cjcrafter.openai.files

import org.jetbrains.annotations.Contract

/**
 * Represents the handler for the files endpoint. This class holds all the
 * actions that can be performed on a file.
 */
interface FileHandler {

    /**
     * Uploads the given file. Returns the data associated with the file,
     * including the id (the id is useful for fine-tuning and assistants).
     *
     * By default, an organization can have up to 100gb of files. If you need
     * more space, you can contact OpenAI support. Individual files can be up
     * to 512mb in size.
     *
     * @param request The request to upload the file
     * @return The uploaded file
     */
    fun upload(request: UploadFileRequest): FileObject

    /**
     * Retrieves the file associated with the given file. This method will
     * return a new instance of the file, and will not modify the given instance.
     *
     * This method is practically useless, as files cannot be modified.
     *
     * @param file The file to retrieve
     * @return The new instance of the retrieved file
     */
    @Contract(pure = true)
    fun retrieve(file: FileObject): FileObject = retrieve(file.id)

    /**
     * Retrieve the file with the given id.
     *
     * @param id The id of the file to retrieve
     * @return The file with the given id
     */
    @Contract(pure = true)
    fun retrieve(id: String): FileObject

    /**
     * Retrieves the content of the given file.
     *
     * @param file The file to retrieve the content of
     * @return The content of the file
     */
    @Contract(pure = true)
    fun retrieveContents(file: FileObject): String = retrieveContents(file.id)

    /**
     * Retrieves the content of the file with the given id.
     *
     * @param id The id of the file to retrieve the content of
     * @return The content of the file
     */
    @Contract(pure = true)
    fun retrieveContents(id: String): String

    /**
     * Attempts to delete the given file. To confirm deletion, you should
     * check [FileDeletionStatus.deleted].
     *
     * @param file The assistant to delete
     * @return The deletion status of the assistant
     */
    fun delete(file: FileObject): FileDeletionStatus = delete(file.id)

    /**
     * Attempts to delete the file with the given id. To confirm deletion,
     * you should check [FileDeletionStatus.deleted].
     *
     * @param id The id of the file to delete
     * @return The deletion status of the file
     */
    fun delete(id: String): FileDeletionStatus

    /**
     * Lists the 20 most recent files.
     *
     * @return The list of files
     */
    @Contract(pure = true)
    fun list(): ListFilesResponse = list(null)

    /**
     * Lists files with the given query parameters.
     *
     * @param request The query parameters
     * @return The list of assistants
     */
    @Contract(pure = true)
    fun list(request: ListFilesRequest?): ListFilesResponse // Cannot use @JvmOverloads in interfaces
}