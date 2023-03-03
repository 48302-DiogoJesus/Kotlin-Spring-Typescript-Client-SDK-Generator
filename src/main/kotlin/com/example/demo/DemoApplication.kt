package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.method.HandlerMethod


@RestController
@RequestMapping("/api/users")
class Users {

    @PostMapping("/")
    fun create(
        @RequestBody requestData: RequestData,
        @PathVariable id: String,
        @RequestParam search: String,
        @RequestParam orderBy: Boolean
    ): ResponseEntity<ResponseData> {
        // process request data, params, and query parameters
        println("${requestData}, ${id}, $search, $orderBy")
        return ResponseEntity.ok().body(ResponseData(output = true))
    }

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: String,
    ): ResponseEntity<Unit> {
        // process request data, params, and query parameters
        println("${id}")
        return ResponseEntity.ok().body(Unit)
    }
}

data class RequestData(val data: String)
data class ResponseData(val output: Boolean)

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}


