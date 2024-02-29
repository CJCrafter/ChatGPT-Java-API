package com.cjcrafter.openai.moderations

import com.cjcrafter.openai.RequestHelper

class ModerationHandlerImpl(
    private val requestHelper: RequestHelper,
    private val endpoint: String,
): ModerationHandler {
    override fun create(request: CreateModerationRequest): Moderation {
        val httpRequest = requestHelper.buildRequest(request, endpoint).build()
        return requestHelper.executeRequest(httpRequest, Moderation::class.java)
    }
}
