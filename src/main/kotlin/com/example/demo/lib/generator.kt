package com.example.demo.lib

import com.example.demo.lib.types.TypeName
import com.example.demo.lib.utils.ConvertToTSFunctionHandler
import com.example.demo.lib.utils.TypeInformation
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping
import java.io.File
import java.time.Instant
import java.util.*

fun generateTypescriptSDKFromSpringHandlers(
    requestMappingHandlerMapping: RequestMappingInfoHandlerMapping,
    buildDirectory: String = "./build-ts-sdk"
) {
    File(buildDirectory).mkdir()

    /**
     * 1. Scan handlers to collect their metadata
     */
    val handlersMetadata =
        buildHandlersMetadata(requestMappingHandlerMapping)

    /**
     * 2. Create type definitions for named types (request body + response body) to be used later
     * Saved to a file "types.d.ts"
     * */
    val userTypes: Set<TypeName> =
        writeTypesFile(handlersMetadata, "$buildDirectory/UserTypes.d.ts")

    /**
     * 3. Build the SDK
     * */
    writeSDKFile(handlersMetadata, userTypes, "$buildDirectory/sdk.ts")

    writeDependencies(
        "$buildDirectory/Utils.ts",
        "$buildDirectory/ServerResponse.ts"
    )
}
