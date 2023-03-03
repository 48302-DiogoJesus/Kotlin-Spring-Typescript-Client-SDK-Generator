package com.example.demo

import com.example.demo.lib.buildHandlersMetadata
import com.example.demo.lib.writeSDKFile
import com.example.demo.lib.writeTypesFile
import com.example.demo.lib.writeUtilsFile
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping

@Component
class HandlerMethodRegistry(requestMappingHandlerMapping: RequestMappingInfoHandlerMapping) {
    init {
        /**
         * 1. Scan handlers to collect their metadata
         */
        val handlersMetadata = buildHandlersMetadata(requestMappingHandlerMapping)

        /**
         * 2. Create type definitions for named types (request body + response body) to be used later
         * Saved to a file "types.d.ts"
         * */
        writeTypesFile(handlersMetadata, "types.d.ts")

        /**
         * 3. Build the SDK
         * */
        writeSDKFile(handlersMetadata, "sdk.ts")

        writeUtilsFile("utils.ts")
    }
}




