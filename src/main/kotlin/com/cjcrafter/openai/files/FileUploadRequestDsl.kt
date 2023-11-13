package com.cjcrafter.openai.files

fun fileUploadRequest(block: FileUploadRequest.Builder.() -> Unit) = FileUploadRequest.builder().apply(block).build()