package clientSdkGenerator.exampleAPI.controllers.errors

import clientSdkGenerator.exampleAPI.utils.ErrorFormat

object GlobalErrors {
    val INVALID_UUID = ErrorFormat(
        internalCode = 333,
        title = "Invalid UUID",
        detail = "The UUID provided is not valid"
    )
}
