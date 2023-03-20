/**
 * Since we are accessing a cloud-based service, there are <i>many</i> steps
 * that can go wrong. This package contains classes the wraps common error
 * codes with instructions to fix or minimize the occurrence of errors. All
 * exceptions inherit from {@link com.cjcrafter.openai.exception.OpenAIException}.
 *
 * @see <a href="https://platform.openai.com/docs/guides/error-codes/api-errors">OpenAI Wiki</a>
 */
package com.cjcrafter.openai.exception;