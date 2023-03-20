/**
 * The chat package stores classes that wrap OpenAI's Chat API. Chat is the
 * model used by ChatGPT (GPT 3.5), and GPT 4.0. As apposed to completions
 * (see {@link com.cjcrafter.openai.completions}), Chat has conversational
 * memory. This allows the AI to reference information from previous messages
 * in the conversation.
 *
 * <p>To use this API, the main class is {@link com.cjcrafter.openai.chat.ChatBot}.
 *
 * @see <a href="https://platform.openai.com/docs/api-reference/chat">OpenAI Wiki</a>
 */
package com.cjcrafter.openai.chat;