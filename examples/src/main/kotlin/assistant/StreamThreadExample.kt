package assistant

import com.cjcrafter.openai.chat.tool.CodeInterpreterToolCall
import com.cjcrafter.openai.chat.tool.FunctionToolCall
import com.cjcrafter.openai.chat.tool.RetrievalToolCall
import com.cjcrafter.openai.openAI
import com.cjcrafter.openai.threads.create
import com.cjcrafter.openai.threads.createRunRequest
import com.cjcrafter.openai.threads.message.ImageContent
import com.cjcrafter.openai.threads.message.TextContent
import com.cjcrafter.openai.threads.message.ThreadUser
import com.cjcrafter.openai.threads.runs.CreateRunRequest
import com.cjcrafter.openai.threads.runs.MessageCreationDetails
import com.cjcrafter.openai.threads.runs.ToolCallsDetails
import io.github.cdimascio.dotenv.dotenv

fun main() {
    // To use dotenv, you need to add the "io.github.cdimascio:dotenv-kotlin:version"
    // dependency. Then you can add a .env file in your project directory.
    val openai = openAI { apiKey(dotenv()["OPENAI_TOKEN"]) }

    // Ask the user to choose an assistant
    val assistants = openai.assistants.list()
    val assistant = assistants.data.first()

    // We have to create a new thread. We'll save this thread, so we can add
    // user messages and get responses later.
    val thread = openai.threads.create()

    openai.threads.messages(thread).create {
        role(ThreadUser.USER)
        content("Hello, world!")
    }
    openai.threads.runs(thread).stream(createRunRequest {
        assistant(assistant)
    })
}
