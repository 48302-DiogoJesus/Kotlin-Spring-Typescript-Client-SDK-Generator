package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/users")
class Users {

    /* @PostMapping("/")
     fun first(
         @RequestBody requestData: RequestData,
         @PathVariable id: String,
         @RequestParam search: String,
         @RequestParam orderBy: Boolean
     ): ResponseEntity<ResponseData> =
         ResponseEntity.ok().body(ResponseData(output = true))*/

    @PostMapping("/{a}/one/{b}/two")
    fun second(
        @RequestBody requestData: RequestData,
        @PathVariable a: String,
        @PathVariable b: String,
        @RequestParam(required = false) search: String?,
        @RequestParam orderBy: Boolean
    ): ResponseEntity<HandlerResponse<ResponseData, ErrorType>>
    // Promise<HandlerResponse<ResponseData, ErrorType>>
    {

        return HandlerResponse.success(ResponseData(output = true))
    }

    /* @GetMapping("/{id}")
     fun third(
         @PathVariable id: String,
     ): ResponseEntity<Unit> =
         ResponseEntity.ok().body(Unit)*/
}

data class ErrorType(val message: String)

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

data class RequestData(val data: String)
data class ResponseData(val output: Boolean)

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}


