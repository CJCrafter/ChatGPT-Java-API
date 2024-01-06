package com.cjcrafter.openai

/**
 * Holds all the available models for the OpenAI API. Most users are probably
 * interested in [Models.Chat].
 *
 * Note that this list is manually updated, and may fall out of date. For the
 * most updated information, check the [OpenAI documentation](https://platform.openai.com/docs/models).
 * If you notice that something is out of date, please [open an issue](https://github.com/CJCrafter/ChatGPT-Java-API/issues).
 *
 * When OpenAI marks a model as _'Legacy'_, the corresponding field in Models
 * will be marked as [Deprecated]. Once it is reported that a model throws an
 * error due to deprecation, the deprecation level will be set to [DeprecationLevel.ERROR].
 */
object Models {

    /**
     * Holds all available Chat models. Chat models are used to generate text
     * in a conversational manner. Chat models work using conversational memory.
     *
     * Note that GPT-4 endpoints are only available to [paying customers](https://help.openai.com/en/articles/7102672-how-can-i-access-gpt-4).
     *
     * @see OpenAI.createChatCompletion
     * @see OpenAI.streamChatCompletion
     * @see OpenAI.assistants
     */
    object Chat {

        /////////////////////////////////////////////////////
        //                    GPT 4.0                      //
        /////////////////////////////////////////////////////

        /**
         * `gpt-4` Turbo. Has a context window of 128,000 tokens with training
         * data up to April 2023. This model has improved instruction following,
         * JSON mode, reproducible output, parallel function calling, and more.
         * Returns a maximum of 4,096 output tokens.
         */
        const val GPT_4_1106_PREVIEW = "gpt-4-1106-preview"

        /**
         * `gpt-4` Turbo with vision. Has a context window of 128,000 tokens with
         * training data up to April 2023. Has the same capabilities as
         * [GPT_4_1106_PREVIEW], but can also understand images.
         */
        const val GPT_4_VISION_PREVIEW = "gpt-4-vision-preview"

        /**
         * Points to the currently supported version of `gpt-4`.
         *
         * See [continuous model upgrades](https://platform.openai.com/docs/models/continuous-model-upgrades)
         */
        const val GPT_4 = "gpt-4"

        /**
         * Points to the currently supported version of `gpt-4` with a 32k context window.
         *
         * See [continuous model upgrades](https://platform.openai.com/docs/models/continuous-model-upgrades)
         */
        const val GPT_4_32k = "gpt-4-32k"

        /**
         * Snapshot of `gpt-4` from June 13th, 2023 with improved function calling
         * support. Has a context window of 8,192 tokens with training data up
         * to September 2021.
         */
        const val GPT_4_0613 = "gpt-4-0613"

        /**
         * Snapshot of `gpt-4-32k` from June 13th, 2023 with improved function
         * calling support. Has a context window of 32,768 tokens with training
         * data up to September 2021.
         */
        const val GPT_4_32k_0613 = "gpt-4-32k-0613"

        /**
         * Snapshot of `gpt-4` from March 14th 2023 with function calling support.
         * This model version will be deprecated on June 13th, 2024. Has a
         * context window of 8,192 tokens with training data up to September
         * 2021.
         */
        @Deprecated(
            message = "This model will be removed on June 13th, 2024",
            replaceWith = ReplaceWith("GPT_4_0613"),
            level = DeprecationLevel.WARNING,
        )
        const val GPT_4_0314 = "gpt-4-0314"

        /**
         * Snapshot of `gpt-4` from March 14th 2023 with function calling support.
         * This model version will be deprecated on June 13th, 2024. Has a
         * context window of 32,768 tokens with training data up to September
         * 2021.
         */
        @Deprecated(
            message = "This model will be removed on June 13th 2024",
            replaceWith = ReplaceWith("GPT_4_32k_0613"),
            level = DeprecationLevel.WARNING,
        )
        const val GPT_4_32k_0314 = "gpt-4-32k-0314"

        /////////////////////////////////////////////////////
        //                    GPT 3.5                      //
        /////////////////////////////////////////////////////

        /**
         * Has a context window of 16,385 tokens with training data up to
         * September 2021. This model has improved instruction following, JSON
         * mode, reproducible outputs, parallel function calling, and more.
         * Returns a maximum of 4,096 output tokens.
         */
        const val GPT_3_5_TURBO_1106 = "gpt_3.5-turbo-1106"

        /**
         * Points to the currently supported version of gpt-3.5-turbo.
         *
         * See [continuous model upgrades](https://platform.openai.com/docs/models/continuous-model-upgrades)
         */
        const val GPT_3_5_TURBO = "gpt-3.5-turbo"

        /**
         * Points to the currently supported version of gpt-3.5-turbo.
         *
         * See [continuous model upgrades](https://platform.openai.com/docs/models/continuous-model-upgrades)
         */
        const val GPT_3_5_TURBO_16k = "gpt-3.5-turbo-16k"

        /**
         * Snapshot of `gpt-3.5-turbo` from June 13th, 2023. This model version
         * will be deprecated on June 13th, 2024. Has a context window of 4,096
         * tokens with training data up to September 2021.
         */
        @Deprecated(
            message = "This model will be removed on June 13th 2024",
            replaceWith = ReplaceWith("GPT_3_5_TURBO_1106"),
            level = DeprecationLevel.WARNING,
        )
        const val GPT_3_5_TURBO_0613 = "gpt-3.5-turbo-0613"

        /**
         * Snapshot of `gpt-3.5-turbo-16k` from June 13th, 2023. This model
         * version will be deprecated on June 13th, 2024. Has a context window
         * of 16,385 tokens with training data up to September 2021.
         */
        @Deprecated(
            message = "This model will be removed on June 13th 2024",
            replaceWith = ReplaceWith("GPT_3_5_TURBO_1106"),
            level = DeprecationLevel.WARNING,
        )
        const val GPT_3_5_TURBO_16k_0613 = "gpt-3.5-turbo-16k-0613"

        /**
         * Snapshot of `gpt-3.5-turbo` from March 1st, 2023. This model version
         * will be deprecated on June 13th, 2024. Has a context window of 4,096
         * tokens with training data up to September 2021.
         */
        @Deprecated(
            message = "This model will be removed on June 13th 2024",
            replaceWith = ReplaceWith("GPT_3_5_TURBO_1106"),
            level = DeprecationLevel.WARNING,
        )
        const val GPT_3_5_TURBO_0301 = "gpt-3.5-turbo-0301"
    }

    /**
     * Holds all available completion models.
     *
     * @see OpenAI.createCompletion
     * @see OpenAI.streamCompletion
     */
    object Completion {

        /////////////////////////////////////////////////////
        //                    GPT 3.5                      //
        /////////////////////////////////////////////////////

        /**
         * Similar to `text-davinci-003` but compatible with the legacy
         * completions endpoint.
         */
        const val GPT_3_5_TURBO_INSTRUCT = "gpt-3.5-turbo-instruct"

        /**
         * Can do language tasks with better quality and consistency than the
         * curie, babbage, or ada models. Will be deprecated on January 4th
         * 2024. Has a context window of 4,096 tokens and training data up to
         * June 2021.
         */
        @Deprecated(
            message = "This model will be removed on January 4th 2024",
            replaceWith = ReplaceWith("GPT_3_5_TURBO_INSTRUCT"),
            level = DeprecationLevel.ERROR,
        )
        const val TEXT_DAVINCI_003 = "text-davinci-003"

        /**
         * Similar capabilities to `text-davinci-003` but trained with
         * supervised fine-tuning instead of reinforcement learning. Will be
         * deprecated on January 4th 2024. Has a context window of 4,096 tokens
         * and training data up to June 2021.
         */
        @Deprecated(
            message = "This model will be removed on January 4th 2024",
            replaceWith = ReplaceWith("GPT_3_5_TURBO_INSTRUCT"),
            level = DeprecationLevel.ERROR,
        )
        const val TEXT_DAVINCI_002 = "text-davinci-002"

        /**
         * Optimized for code-completion tasks. Will be deprecated on Jan 4th
         * 2024. Has a context window of 8,001 tokens and training data up to
         * June 2021.
         */
        @Deprecated(
            message = "This model will be removed on January 4th 2024",
            replaceWith = ReplaceWith("GPT_3_5_TURBO_INSTRUCT"),
            level = DeprecationLevel.ERROR,
        )
        const val CODE_DAVINCI_002 = "code-davinci-002"

        /////////////////////////////////////////////////////
        //                    GPT 3.0                      //
        /////////////////////////////////////////////////////

        /**
         * Vary capable, faster and lower cost than `davinci`. Has a context
         * window of 2,049 tokens and training data up to October 2019.
         */
        const val TEXT_CURIE_001 = "text-curie-001"

        /**
         * Capable of straightforward tasks, very fast, and lower cost. Has a
         * context window of 2,049 tokens and training data up to October 2019.
         */
        const val TEXT_BABBAGE_001 = "text-babbage-001"

        /**
         * Capable of very simple tasks, usually the fastest model in  the `gpt-3`
         * series, and lowest cost. Has a context window of 2,049 tokens and
         * training data up to October 2019.
         */
        const val TEXT_ADA_001 = "text-ada-001"

        /**
         * Most capable `gpt-3` model, can do any task the other models can do,
         * often with higher quality. Can be fine-tuned. Has a context window of
         * 2,049 tokens and training data up to October 2019.
         */
        const val DAVINCI = "davinci"

        /**
         * Very capable, but faster and lower cost than `davinci`. Can be
         * fine-tuned. Has a context window of 2,049 tokens and training
         * data up to October 2019.
         */
        const val CURIE = "curie"

        /**
         * Capable of straightforward tasks, very fast, and lower cost. Can be
         * fine-tuned. Has a context window of 2,049 tokens and training data
         * up to October 2019.
         */
        const val BABBAGE = "babbage"

        /**
         * Capable of very simple tasks, usually the fasted model in the `gpt-3`
         * series, and lowest cost. Can be fine-tuned. Has a context window of
         * 2,049 tokens and training data up to October 2019.
         */
        const val ADA = "ada"
    }
}