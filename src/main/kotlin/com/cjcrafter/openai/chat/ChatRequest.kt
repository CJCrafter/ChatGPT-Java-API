package com.cjcrafter.openai.chat

import com.cjcrafter.openai.chat.tool.AbstractTool
import com.cjcrafter.openai.chat.tool.FunctionTool
import com.cjcrafter.openai.chat.tool.Tool
import com.cjcrafter.openai.chat.tool.ToolChoice
import com.cjcrafter.openai.util.OpenAIDslMarker
import com.fasterxml.jackson.annotation.JsonProperty

data class ChatRequest @JvmOverloads internal constructor(
    var messages: MutableList<ChatMessage>,
    var model: String,
    @JsonProperty("frequency_penalty") var frequencyPenalty: Float? = null,
    @JsonProperty("logit_bias") var logitBias: MutableMap<String, Float>? = null,
    @JsonProperty("max_tokens") var maxTokens: Int? = null,
    var n: Int? = null,
    @JsonProperty("presence_penalty") var presencePenalty: Float? = null,
    @JsonProperty("response_format") var responseFormat: ChatResponseFormat? = null,
    var seed: Int? = null,
    var stop: String? = null,
    @Deprecated("Use OpenAI#streamChatCompletion") var stream: Boolean? = null,
    var temperature: Float? = null,
    @JsonProperty("top_p") var topP: Float? = null,
    var tools: MutableList<Tool>? = null,
    @JsonProperty("tool_choice") var toolChoice: ToolChoice? = null,
    var user: String? = null,
) {

    /**
     * [Builder] is a helper class to build a [ChatRequest] instance with a stable API.
     * It provides methods for setting the properties of the [ChatRequest] object.
     * The [build] method returns a new [ChatRequest] instance with the specified properties.
     *
     * Usage:
     * ```
     * val chatRequest = ChatRequest.builder()
     *     .model("gpt-3.5-turbo")
     *     .messages(mutableListOf("Be as helpful as possible".toSystemMessage()))
     *     .build()
     * ```
     */
    @OpenAIDslMarker
    class Builder internal constructor() {
        private var messages: MutableList<ChatMessage>? = null
        private var model: String? = null
        private var frequencyPenalty: Float? = null
        private var logitBias: MutableMap<String, Float>? = null
        private var maxTokens: Int? = null
        private var n: Int? = null
        private var presencePenalty: Float? = null
        private var responseFormat: ChatResponseFormat? = null
        private var seed: Int? = null
        private var stop: String? = null
        private var stream: Boolean? = null
        private var temperature: Float? = null
        private var topP: Float? = null
        private var tools: MutableList<Tool>? = null
        private var toolChoice: ToolChoice? = null
        private var user: String? = null

        fun messages(messages: MutableList<ChatMessage>) = apply { this.messages = messages }
        fun model(model: String) = apply { this.model = model }
        fun frequencyPenalty(frequencyPenalty: Float) = apply { this.frequencyPenalty = frequencyPenalty }
        fun logitBias(logitBias: MutableMap<String, Float>) = apply { this.logitBias = logitBias }
        fun maxTokens(maxTokens: Int) = apply { this.maxTokens = maxTokens }
        fun n(n: Int) = apply { this.n = n }
        fun presencePenalty(presencePenalty: Float) = apply { this.presencePenalty = presencePenalty }
        fun responseFormat(responseFormat: ChatResponseFormat) = apply { this.responseFormat = responseFormat }
        fun seed(seed: Int) = apply { this.seed = seed }
        fun stop(stop: String) = apply { this.stop = stop }
        fun stream(stream: Boolean) = apply { this.stream = stream }
        fun temperature(temperature: Float) = apply { this.temperature = temperature }
        fun topP(topP: Float) = apply { this.topP = topP }
        fun tools(tools: MutableList<Tool>) = apply { this.tools = tools }
        fun toolChoice(toolChoice: ToolChoice) = apply { this.toolChoice = toolChoice }
        fun user(user: String) = apply { this.user = user }

        /**
         * Adds a message to the end of the list.
         */
        fun addMessage(message: ChatMessage) = apply {
            if (messages == null) messages = mutableListOf()
            messages!!.add(message)
        }

        /**
         * Adds a tool to the end of the list.
         *
         * @param tool
         */
        fun addTool(tool: AbstractTool) = apply {
            if (tools == null) tools = mutableListOf()
            tools!!.add(tool.toTool())
        }

        /**
         * Blocks ChatGPT from using any tools.
         */
        fun useNoTool() = apply {
            toolChoice = ToolChoice.None
        }

        /**
         * Lets ChatGPT decide whether to use tools.
         */
        fun useAutoTool() = apply {
            toolChoice = ToolChoice.Auto
        }

        /**
         * Forces ChatGPT to call the specified function.
         */
        fun useFunctionTool(name: String) = apply {
            toolChoice = ToolChoice.Function(name)
        }

        fun build(): ChatRequest {
            return ChatRequest(
                messages = messages ?: throw IllegalStateException("messages must be set"),
                model = model ?: throw IllegalStateException("model must be set"),
                frequencyPenalty = frequencyPenalty,
                logitBias = logitBias,
                maxTokens = maxTokens,
                n = n,
                presencePenalty = presencePenalty,
                responseFormat = responseFormat,
                seed = seed,
                stop = stop,
                stream = stream,
                temperature = temperature,
                topP = topP,
                tools = tools,
                toolChoice = toolChoice,
                user = user,
            )
        }
    }

    companion object {

        /**
         * Instantiates a new [ChatRequest] builder instance. Make sure you set
         * the 2 required values:
         * 1. [Builder.model]
         * 2. [Builder.messages]
         */
        @JvmStatic
        fun builder(): Builder = Builder()
    }
}