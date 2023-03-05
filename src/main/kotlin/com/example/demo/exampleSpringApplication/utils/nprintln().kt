package com.example.demo.exampleSpringApplication.utils

fun nprintln(vararg data: Any?) {
    println(data.joinToString(" ") { it.toString() })
}
