package com.cjcrafter.openai.exception

import java.io.IOException

class WrappedIOError(val exception: IOException) : OpenAIError(null, null, exception.message ?: "")