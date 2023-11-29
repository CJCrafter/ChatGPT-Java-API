package com.cjcrafter.openai.files

fun uploadFileRequest(block: UploadFileRequest.Builder.() -> Unit) = UploadFileRequest.builder().apply(block).build()