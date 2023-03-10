package kttsRPC.exampleAPI.controllers

import kttsRPC.exampleAPI.controllers.errors.GlobalErrors
import kttsRPC.exampleAPI.controllers.errors.UserErrors
import kttsRPC.exampleAPI.utils.Uris
import kttsRPC.types.HandlerResponse
import kttsRPC.types.HandlerResponseType
import org.springframework.web.bind.annotation.*
import java.time.Instant
import java.util.UUID

data class User(
    val id: UUID,

    val name: String,
    val createdAt: Instant = Instant.now()
)

val USERS_DB: MutableMap<UUID, User> = mutableMapOf()

@RestController
@RequestMapping(Uris.Users.BASE)
class Users {

    @GetMapping(Uris.Users.GET)
    fun get(
        // (name = "id") is not needed if the variable name is "id" instead of "userId"
        // Here it's just used to show it's possible
        @PathVariable(name = "id") userId: String
    ): HandlerResponseType<User> {
        val uuid = UUID.fromString(userId)
            ?: return HandlerResponse.error(
                400,
                GlobalErrors.INVALID_UUID
            )

        val user: User =
            USERS_DB[uuid]
                ?: return HandlerResponse.error(
                    404,
                    UserErrors.USER_NOT_FOUND_ERROR
                )

        return HandlerResponse.success(user)
    }

    data class CreateUserModel(val name: String)

    @PostMapping(Uris.Users.CREATE)
    fun create(
        @RequestBody user: Users.CreateUserModel
    ): HandlerResponseType<User> {
        if (user.name.length <= 4) {
            return HandlerResponse.error(
                400,
                UserErrors.USER_NAME_LENGTH_ERROR
            )
        }

        val newUserId = UUID.randomUUID()
        val newUser: User =
            User(
                id = newUserId,
                name = user.name
                // createdAt automatically generated
            )

        // Persist user
        USERS_DB[newUserId] = newUser

        return HandlerResponse.success(newUser)
    }

    @DeleteMapping(Uris.Users.DELETE)
    fun delete(
        @PathVariable(name = "id") userId: String
    ): HandlerResponseType<Unit> {
        val uuid = UUID.fromString(userId)
            ?: return HandlerResponse.error(
                400,
                GlobalErrors.INVALID_UUID
            )

        val userToDelete: User =
            USERS_DB[uuid]
                ?: return HandlerResponse.error(
                    404,
                    UserErrors.USER_NOT_FOUND_ERROR
                )

        // Remove from DB
        USERS_DB.remove(uuid)

        return HandlerResponse.success(Unit)
    }
}
