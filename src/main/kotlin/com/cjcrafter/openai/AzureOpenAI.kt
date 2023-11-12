package com.cjcrafter.openai

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.jetbrains.annotations.ApiStatus

/**
 * The Azure OpenAI API client.
 *
 * See {@link OpenAI} for more information.
 *
 * This class constructs url in the form of: https://<azureBaseUrl>/openai/deployments/<modelName>/<endpoint>?api-version=<apiVersion>
 *
 * @property apiVersion The API version to use. Defaults to 2023-03-15-preview.
 * @property modelName The model name to use. This is the name of the model deployed to Azure.
 */
class AzureOpenAI @ApiStatus.Internal constructor(
    apiKey: String,
    organization: String? = null,
    client: OkHttpClient = OkHttpClient(),
    baseUrl: String = "https://api.openai.com",
    private val apiVersion: String = "2023-03-15-preview",
    private val modelName: String = ""
) : OpenAIImpl(apiKey, organization, client, baseUrl) {

    override fun buildRequest(request: Any, endpoint: String): Request {
        val json = objectMapper.writeValueAsString(request)
        val body: RequestBody = json.toRequestBody(mediaType)
        return Request.Builder()
            .url("$baseUrl/openai/deployments/$modelName/$endpoint?api-version=$apiVersion")
            .addHeader("Content-Type", "application/json")
            .addHeader("api-key", apiKey)
            .apply { if (organization != null) addHeader("OpenAI-Organization", organization) }
            .post(body).build()
    }
}