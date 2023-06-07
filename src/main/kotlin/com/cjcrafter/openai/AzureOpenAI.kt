package com.cjcrafter.openai

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * The Azure OpenAI API client.
 *
 * See {@link OpenAI} for more information.
 *
 * This class constructs url in the form of: https://<azureBaseUrl>/openai/deployments/<modelName>/<endpoint>?api-version=<apiVersion>
 *
 * @property azureBaseUrl The base URL for the Azure OpenAI API. Usually https://<your_resource_group>.openai.azure.com
 * @property apiVersion The API version to use. Defaults to 2023-03-15-preview.
 * @property modelName The model name to use. This is the name of the model deployed to Azure.
 */
class AzureOpenAI @JvmOverloads constructor(
    apiKey: String,
    organization: String? = null,
    client: OkHttpClient = OkHttpClient(),
    private val azureBaseUrl: String = "",
    private val apiVersion: String = "2023-03-15-preview",
    private val modelName: String = ""
) : OpenAI(apiKey, organization, client) {

    override fun buildRequest(request: Any, endpoint: String): Request {
        val removedV1Endpoint = endpoint.removePrefix("v1/") // temporary fix for Azure, as it doesn't support v1/ in the url
        val json = gson.toJson(request)
        val body: RequestBody = json.toRequestBody(mediaType)
        return Request.Builder()
            .url("$azureBaseUrl/openai/deployments/$modelName/$removedV1Endpoint?api-version=$apiVersion")
            .addHeader("Content-Type", "application/json")
            .addHeader("api-key", apiKey)
            .apply { if (organization != null) addHeader("OpenAI-Organization", organization) }
            .post(body).build()
    }

}