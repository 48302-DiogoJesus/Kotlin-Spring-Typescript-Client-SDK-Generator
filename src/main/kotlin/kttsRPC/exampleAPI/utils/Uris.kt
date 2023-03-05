package kttsRPC.exampleAPI.utils

object Uris {
    const val BASE = "/api"

    object Users {
        const val BASE = "${Uris.BASE}/users"

        const val CREATE = "${Uris.Users.BASE}/"
        const val GET = "${Uris.Users.BASE}/{id}"
        const val DELETE = "${Uris.Users.BASE}/{id}"
    }

    object Posts {
        const val BASE = "${Uris.BASE}/posts"

        const val GET = "${Uris.Posts.BASE}/{id}"
        const val CREATE = "${Uris.Posts.BASE}/"
    }
}
