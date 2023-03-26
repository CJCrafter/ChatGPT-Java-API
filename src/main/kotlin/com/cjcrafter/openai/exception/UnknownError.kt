package com.cjcrafter.openai.exception

import com.google.gson.JsonElement

class UnknownError(param: JsonElement?, code: String?, message: String, val type: String?) : OpenAIError(param, code, message)