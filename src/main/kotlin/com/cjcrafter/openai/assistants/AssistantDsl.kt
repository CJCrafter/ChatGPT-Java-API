package com.cjcrafter.openai.assistants

fun createAssistantRequest(block: CreateAssistantRequest.Builder.() -> Unit): CreateAssistantRequest {
    return CreateAssistantRequest.builder().apply(block).build()
}

fun modifyAssistantRequest(block: ModifyAssistantRequest.Builder.() -> Unit): ModifyAssistantRequest {
    return ModifyAssistantRequest.builder().apply(block).build()
}

fun listAssistantRequest(block: ListAssistantRequest.Builder.() -> Unit): ListAssistantRequest {
    return ListAssistantRequest.builder().apply(block).build()
}