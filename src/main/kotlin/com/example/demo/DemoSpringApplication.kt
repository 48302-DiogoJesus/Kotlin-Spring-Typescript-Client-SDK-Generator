package com.example.demo

import com.example.demo.lib.types.HandlerResponse
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
        @PathVariable(required = false) a: String?,
        @PathVariable b: String,
        @RequestParam(required = false) search: String?,
        @RequestParam orderBy: Boolean
    ): ResponseEntity<HandlerResponse<String, String>> {

        return HandlerResponse.success("hello")
    }

    /* @GetMapping("/{id}")
     fun third(
         @PathVariable id: String,
     ): ResponseEntity<Unit> =
         ResponseEntity.ok().body(Unit)*/
}

data class ErrorType(val message: String)

data class RequestData(val data: String)
data class ResponseData(val output: Boolean)

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}


