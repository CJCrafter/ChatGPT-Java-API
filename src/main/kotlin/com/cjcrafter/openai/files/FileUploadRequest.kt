package com.cjcrafter.openai.files

import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream

data class FileUploadRequest internal constructor(
    var fileName: String,
    var requestBody: RequestBody,
    var purpose: FilePurpose,
) {

    class Builder internal constructor() {
        private var fileName: String? = null
        private var requestBody: RequestBody? = null
        private var purpose: FilePurpose? = null

        fun fileName(fileName: String) = apply { this.fileName = fileName }
        fun requestBody(requestBody: RequestBody) = apply { this.requestBody = requestBody }
        fun purpose(purpose: FilePurpose) = apply { this.purpose = purpose }

        /**
         * Uploads a java.io.File as a file.
         *
         * @param file The file to upload
         */
        fun file(file: java.io.File) = apply {
            fileName = file.name
            requestBody = file.asRequestBody()
        }

        /**
         * Uploads a stream as a file. This loads the entire stream into memory,
         * so this method is not recommended for large files.
         *
         * @param fileName The name of the file
         * @param stream The input stream of data
         */
        fun stream(fileName: String, stream: InputStream) = apply {
            this.fileName = fileName
            requestBody = stream.readBytes().toRequestBody()
        }

        /**
         * Uploads a byte array as a file.
         *
         * @param fileName The name of the file
         * @param bytes The byte array of data
         */
        fun byteArray(fileName: String, bytes: ByteArray) = apply {
            this.fileName = fileName
            requestBody = bytes.toRequestBody()
        }

        /**
         * Uploads string contents as a file.
         *
         * @param fileName The name of the file
         * @param string The content of the file as a string
         */
        fun string(fileName: String, string: String) = apply {
            this.fileName = fileName
            requestBody = string.toRequestBody()
        }

        fun build(): FileUploadRequest {
            return FileUploadRequest(
                fileName = fileName ?: throw IllegalStateException("fileName must be defined to use FileUploadRequest"),
                requestBody = requestBody!!,
                purpose = purpose!!
            )
        }
    }

    companion object {

        /**
         * Creates a [FileUploadRequest.Builder] to build a [FileUploadRequest].
         */
        @JvmStatic
        fun builder() = Builder()
    }
}