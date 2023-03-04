package com.example.demo.lib

import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping
import java.io.File

fun generateTypescriptSDKFromSpringHandlers(
    requestMappingHandlerMapping: RequestMappingInfoHandlerMapping,
    buildDirectory: String = "./build-ts-sdk"
) {
    File(buildDirectory).mkdir()

    /**
     * 1. Scan handlers to collect their metadata
     */
    val handlersMetadata = buildHandlersMetadata(requestMappingHandlerMapping)

    /**
     * 2. Create type definitions for named types (request body + response body) to be used later
     * Saved to a file "types.d.ts"
     * */
    writeTypesFile(handlersMetadata, "$buildDirectory/UserTypes.d.ts")

    /**
     * 3. Build the SDK
     * */
    writeSDKFile(handlersMetadata, "$buildDirectory/sdk.ts")

    writeDependencies(
        "$buildDirectory/Utils.ts",
        "$buildDirectory/ServerResponse.ts"
    )
}