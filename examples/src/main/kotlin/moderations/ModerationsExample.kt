package moderations

import com.cjcrafter.openai.moderations.create
import com.cjcrafter.openai.openAI
import io.github.cdimascio.dotenv.dotenv


fun main() {

    // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
    // dependency. Then you can add a .env file in your project directory.
    val key = dotenv()["OPENAI_TOKEN"]
    val openai = openAI { apiKey(key) }

    while (true) {
        print("Input: ")
        val input = readln()
        val moderation = openai.moderations.create {
            input(input)
        }

        val max = moderation.results[0].categoryScores.entries.maxBy { it.value }
        println("Highest category: ${max.key} with a score of ${max.value}")
    }
}