package com.example.demo.exampleSpringApplication.controllers.errors

import com.example.demo.exampleSpringApplication.utils.ErrorFormat

object GlobalErrors {
    val INVALID_UUID = ErrorFormat(
        internalCode = 333,
        title = "Invalid UUID",
        detail = "The UUID provided is not valid"
    )
}
