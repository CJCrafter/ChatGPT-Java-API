package com.cjcrafter.openai.threads.message

import com.fasterxml.jackson.annotation.JsonProperty

data class ImageContent(
    @JsonProperty("image_file", required = true) val imageFile: ImageFile,
): ThreadMessageContent() {

    data class ImageFile(
        @JsonProperty("file_id", required = true) val fileId: String,
    )
}