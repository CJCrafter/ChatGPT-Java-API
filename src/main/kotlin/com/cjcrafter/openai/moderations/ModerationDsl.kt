package com.cjcrafter.openai.moderations

fun createModerationRequest(block: CreateModerationRequest.Builder.() -> Unit): CreateModerationRequest {
    return CreateModerationRequest.builder().apply(block).build()
}

fun ModerationHandler.create(block: CreateModerationRequest.Builder.() -> Unit): Moderation {
    val request = createModerationRequest(block)
    return create(request)
}