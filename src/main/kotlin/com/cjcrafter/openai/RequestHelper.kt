package com.cjcrafter.openai

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

open class RequestHelper(
    protected val apiKey: String,
    protected val organization: String? = null,
    protected val client: OkHttpClient = OkHttpClient(),
    protected val baseUrl: String = "https://api.openai.com",
) {
    protected val mediaType = "application/json; charset=utf-8".toMediaType()
    protected val objectMapper = OpenAI.createObjectMapper()

    open fun buildRequest(request: Any, endpoint: String): Request.Builder {
        val json = objectMapper.writeValueAsString(request)
        val body: RequestBody = json.toRequestBody(mediaType)
        return Request.Builder()
            .url("$baseUrl/$endpoint")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .apply { if (organization != null) addHeader("OpenAI-Organization", organization) }
            .post(body)
    }

    open fun buildRequestNoBody(endpoint: String, params: Map<String, Any>? = null): Request.Builder {
        val url = "$baseUrl/$endpoint".toHttpUrl().newBuilder()
            .apply {
                params?.forEach { (key, value) -> addQueryParameter(key, value.toString()) }
            }.build().toString()

        return Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $apiKey")
            .apply { if (organization != null) addHeader("OpenAI-Organization", organization) }
    }

    open fun buildMultipartRequest(
        endpoint: String,
        function: MultipartBody.Builder.() -> Unit,
    ): Request {

        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .apply(function)
            .build()

        return Request.Builder()
            .url("$baseUrl/$endpoint")
            .addHeader("Authorization", "Bearer $apiKey")
            .apply { if (organization != null) addHeader("OpenAI-Organization", organization) }
            .post(multipartBody).build()
    }

    open fun executeRequest(httpRequest: Request): String {
        val httpResponse = client.newCall(httpRequest).execute()
        if (!httpResponse.isSuccessful) {
            val json = httpResponse.body?.byteStream()?.bufferedReader()?.readText()
            httpResponse.close()
            throw IOException("Unexpected code $httpResponse, received: $json")
        }

        val jsonReader = httpResponse.body?.byteStream()?.bufferedReader()
            ?: throw IOException("Response body is null")
        val responseStr = jsonReader.readText()
        OpenAI.logger.debug(responseStr)
        return responseStr
    }

    open fun <T> executeRequest(httpRequest: Request, responseType: Class<T>): T {
        val str = executeRequest(httpRequest)
        return objectMapper.readValue(str, responseType)
    }
}