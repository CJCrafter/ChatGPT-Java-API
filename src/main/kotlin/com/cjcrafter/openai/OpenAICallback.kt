package com.cjcrafter.openai

import com.cjcrafter.openai.exception.OpenAIError
import com.cjcrafter.openai.exception.WrappedIOError
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException
import java.util.function.Consumer

internal class OpenAICallback(
    private val isStream: Boolean,
    private val onFailure: Consumer<OpenAIError>,
    private val onResponse: Consumer<JsonObject>
) : Callback {

    override fun onFailure(call: Call, e: IOException) {
        onFailure.accept(WrappedIOError(e))
    }

    override fun onResponse(call: Call, response: Response) {
        onResponse(response)
    }

    fun onResponse(response: Response) {
        if (isStream) {
            handleStream(response)
            return
        }

        val rootObject = JsonParser.parseString(response.body!!.string()).asJsonObject

        // Sometimes OpenAI will respond with an error code for malformed
        // requests, timeouts, rate limits, etc. We need to let the dev
        // know that an error occurred.
        if (rootObject.has("error")) {
            onFailure.accept(OpenAIError.fromJson(rootObject.get("error").asJsonObject))
            return
        }

        onResponse.accept(rootObject)
    }

    private fun handleStream(response: Response) {
        response.body?.source()?.use { source ->

            while (!source.exhausted()) {
                var jsonResponse = source.readUtf8()

                // Or data is separated by empty lines, ignore them. The final
                // line is always "data: [DONE]", ignore it.
                if (jsonResponse.isNullOrEmpty() || jsonResponse == "data: [DONE]")
                    continue

                // The CHAT API returns a json string, but they prepend the content
                // with "data: " (which is not valid json). In order to parse this
                // into a JsonObject, we have to strip away this extra string.
                if (jsonResponse.startsWith("data: "))
                    jsonResponse = jsonResponse.substring("data: ".length)

                lateinit var rootObject: JsonObject
                try {
                    rootObject = JsonParser.parseString(jsonResponse).asJsonObject
                } catch (ex: JsonParseException) {
                    println(jsonResponse)
                    ex.printStackTrace()
                    continue
                }

                // Sometimes OpenAI will respond with an error code for malformed
                // requests, timeouts, rate limits, etc. We need to let the dev
                // know that an error occurred.
                if (rootObject.has("error")) {
                    onFailure.accept(OpenAIError.fromJson(rootObject.get("error").asJsonObject))
                    continue
                }

                // Developer defined code to run
                onResponse.accept(rootObject)
            }
        }
    }
}
