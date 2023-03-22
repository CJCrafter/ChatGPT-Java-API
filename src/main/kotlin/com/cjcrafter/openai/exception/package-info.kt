/**
 * Since we are accessing a cloud-based service, there are *many* steps
 * that can go wrong. This package contains classes the wraps common error
 * codes with instructions to fix or minimize the occurrence of errors. All
 * exceptions inherit from [com.cjcrafter.openai.exception.OpenAIException].
 *
 * @see [OpenAI Wiki](https://platform.openai.com/docs/guides/error-codes/api-errors)
 */
package com.cjcrafter.openai.exception
