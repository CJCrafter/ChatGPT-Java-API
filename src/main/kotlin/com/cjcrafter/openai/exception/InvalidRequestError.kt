package com.cjcrafter.openai.exception

import com.google.gson.JsonElement

class InvalidRequestError(param: JsonElement?, code: String?, message: String) : OpenAIError(param, code, message)