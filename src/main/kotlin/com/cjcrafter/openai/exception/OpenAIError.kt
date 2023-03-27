package com.cjcrafter.openai.exception

import com.google.gson.JsonElement
import com.google.gson.JsonObject

// TODO docs
abstract class OpenAIError : Exception {

    val param: JsonElement?
    val code: String?

    constructor(param: JsonElement?, code: String?, message: String) : super(message) {
        this.param = param
        this.code = code
    }

    constructor(param: JsonElement?, code: String?, message: String, cause: Throwable?) : super(message, cause) {
        this.param = param
        this.code = code
    }

    constructor(param: JsonElement?, code: String?, cause: Throwable?) : super(cause) {
        this.param = param
        this.code = code
    }

    companion object {

        @JvmStatic
        fun fromJson(json: JsonObject) : OpenAIError {
            val message = json["message"].asString
            val type = if (json["type"].isJsonNull) null else json["type"].asString
            val param = if (json["param"].isJsonNull) null else json["param"]
            val code = if (json["code"].isJsonNull) null else json["code"].asString

            // TODO add more error types
            return when (json["type"].asString) {
                "invalid_request_error" -> InvalidRequestError(param, code, message)
                else -> UnknownError(param, code, message, type)
            }
        }
    }
}