package com.example.demo.exampleSpringApplication.utils

object Uris {
    const val BASE = "/api"

    object Users {
        const val BASE = "${Uris.BASE}/users"

        const val CREATE = "$BASE/"
        const val GET = "$BASE/{id}"
        const val DELETE = "$BASE/{id}"
    }
}
