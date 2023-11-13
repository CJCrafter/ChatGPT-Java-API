package embeddings

import com.cjcrafter.openai.embeddings.embeddingsRequest
import com.cjcrafter.openai.openAI
import io.github.cdimascio.dotenv.dotenv

/**
 * In this Kotlin example, we will be using the embeddings API to generate the
 * embeddings of a list of strings.
 */
fun main() {

    // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
    // dependency. Then you can add a .env file in your project directory.
    val key = dotenv()["OPENAI_TOKEN"]
    val openai = openAI { apiKey(key) }

    val request = embeddingsRequest {
        input("hi")
        model("text-embedding-ada-002")
    }

    val response = openai.createEmbeddings(request)
    println(response)
}