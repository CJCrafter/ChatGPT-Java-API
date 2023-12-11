package assistant

import com.cjcrafter.openai.openAI
import com.cjcrafter.openai.threads.create
import com.cjcrafter.openai.threads.message.ThreadUser
import io.github.cdimascio.dotenv.dotenv

fun main() {
    // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
    // dependency. Then you can add a .env file in your project directory.
    val openai = openAI { apiKey(dotenv()["OPENAI_TOKEN"]) }

    // Ask the user to choose an assistant
    val assistants = openai.assistants.list()
    assistants.data.forEachIndexed { index, assistant -> println("$index. $assistant") }
    val choice = readln().toInt()
    val assistant = assistants.data[choice]

    // We have to create a new thread. We'll save this thread, so we can add
    // user messages and get responses later.
    val thread = openai.threads.create()

    while (true) {

        // Handle user input
        println("Type your input below: ")
        val input = readln()
        openai.threads.messages(thread).create {
            role(ThreadUser.USER)
            content(input)

            // You can also add files and metadata to the message
            //addFile(file)
            //addMetadata("key", "value")
        }

        // After adding a message to the thread, we have to "run" the thread
        var run = openai.threads.runs(thread).create {
            assistant(assistant)
            //instructions("You can override instructions, model, etc.")
        }

        // This is a known limitation in OpenAI, and they are working to address
        // this so that we can easily stream a response without nonsense like this.
        while (!run.status.isTerminal) {
            Thread.sleep(2500)
            run = openai.threads.runs(thread).retrieve(run)
        }
    }

}
