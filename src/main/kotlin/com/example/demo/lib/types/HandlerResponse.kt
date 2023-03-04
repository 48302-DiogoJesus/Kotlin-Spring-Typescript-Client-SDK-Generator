package com.example.demo.lib.types

import org.springframework.http.ResponseEntity

data class HandlerResponse<S, E> private constructor(
    val isSuccess: Boolean,
    val data: S?,
    val error: E?,
) {
    companion object {
        fun <S, E> success(data: S, statusCode: Int = 200) =
            ResponseEntity
                .status(statusCode)
                .body(HandlerResponse<S, E>(true, data, null))

        fun <S, E> error(error: E, statusCode: Int) =
            ResponseEntity
                .status(statusCode)
                .body(HandlerResponse<S, E>(true, null, error))
    }
}