package com.example.demo

import com.example.demo.lib.nprintln
import com.example.demo.lib.types.HandlerResponse
import org.apache.coyote.Response
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp
import java.time.Instant


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
    ): ResponseEntity<HandlerResponse<ResponseData, String>> {
        // nprintln(a, b, search, orderBy, requestData)
        return HandlerResponse.success(ResponseData(Timestamp.from(Instant.now())))
    }

    /* @GetMapping("/{id}")
     fun third(
         @PathVariable id: String,
     ): ResponseEntity<Unit> =
         ResponseEntity.ok().body(Unit)*/
}

data class ErrorType(val message: String)

data class RequestData(val data: List<Int>)
data class ResponseData(val date: Timestamp)

@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}


