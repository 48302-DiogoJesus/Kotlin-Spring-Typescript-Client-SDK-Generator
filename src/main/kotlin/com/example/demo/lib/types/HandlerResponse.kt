package com.example.demo.lib.types

import com.example.demo.exampleSpringApplication.utils.ErrorFormat
import org.springframework.http.ResponseEntity

// If the API has a global error format it's more readable/concise to fix it like this
typealias HandlerResponseType<S> = ResponseEntity<HandlerResponse<S, ErrorFormat>>

class HandlerResponse<S, E> private constructor(
    val isSuccess: Boolean,
    val data: S?,
    val error: E?,
) {
    companion object {
        fun <S, E> success(data: S, statusCode: Int = 200) =
            ResponseEntity
                .status(statusCode)
                .body(HandlerResponse<S, E>(true, data, null))

        fun <S, E> error(statusCode: Int, error: E) =
            ResponseEntity
                .status(statusCode)
                .body(HandlerResponse<S, E>(false, null, error))
    }
}