package com.example.demo.exampleSpringApplication.controllers.errors

import com.example.demo.exampleSpringApplication.utils.ErrorFormat

object UserErrors {
    val USER_NOT_FOUND_ERROR = ErrorFormat(
        internalCode = 111,
        title = "User does not exist",
        detail = "The user id provided does not correspond to an existing user"
    )

    val USER_NAME_LENGTH_ERROR = ErrorFormat(
        internalCode = 222,
        title = "User name length error",
        detail = "The user name length is not within bounds. It should be greater than 5 characters"
    )
}
