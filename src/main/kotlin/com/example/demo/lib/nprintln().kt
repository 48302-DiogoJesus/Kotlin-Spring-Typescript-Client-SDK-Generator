package com.example.demo.lib

fun nprintln(vararg data: Any?) {
    println(data.joinToString(" ") { it.toString() })
}