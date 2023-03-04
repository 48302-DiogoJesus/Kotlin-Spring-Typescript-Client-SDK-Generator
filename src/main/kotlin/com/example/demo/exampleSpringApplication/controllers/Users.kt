package com.example.demo.exampleSpringApplication.controllers

import com.example.demo.exampleSpringApplication.*
import com.example.demo.exampleSpringApplication.controllers.errors.UserErrors
import com.example.demo.exampleSpringApplication.utils.Uris
import com.example.demo.lib.types.HandlerResponse
import com.example.demo.lib.types.HandlerResponseType
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    // See if it works on the client
    val createdAt: Instant = Instant.now()
)


val USERS_DB: MutableMap<UUID, User> = mutableMapOf()

@RestController
@RequestMapping(Uris.Users.BASE)
class USERS {

    @GetMapping(Uris.Users.GET)
    fun get(
        @PathVariable id: UUID,
    ): HandlerResponseType<User> {
        val user: User = USERS_DB[id]
            ?: return HandlerResponse.error(404, UserErrors.USER_NOT_FOUND_ERROR)

        return HandlerResponse.success(user)
    }

    data class CreateUserModel(val name: String)

    @PostMapping(Uris.Users.CREATE)
    fun create(
        @RequestBody user: CreateUserModel,
    ): HandlerResponseType<User> {
        if (user.name.length < 4)
            return HandlerResponse.error(400, UserErrors.USER_NAME_LENGTH_ERROR)

        val newUserId = UUID.randomUUID()
        val newUser: User = User(
            id = newUserId,
            name = user.name
            // createdAt automatically generated
        )

        // Persist user
        USERS_DB[newUserId] = newUser

        return HandlerResponse.success(newUser)
    }

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable(name = "id") userId: UUID,
    ): HandlerResponseType<Unit> {
        val userToDelete: User = USERS_DB[userId]
            ?: return HandlerResponse.error(404, UserErrors.USER_NOT_FOUND_ERROR)

        // Remove from DB
        USERS_DB.remove(userId)

        return HandlerResponse.success(Unit)
    }
}