package com.example.demo.exampleSpringApplication

import com.example.demo.lib.*
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping

/**
 * Library clients would have to somehow get the RequestMappingInfoHandlerMapping to call my generator:
 * generateTypescriptSDKFromSpringHandlers
 */
@Component
class TypescriptSDKGenerator(requestMappingHandlerMapping: RequestMappingInfoHandlerMapping) {
    // * Remove when used as jar library
    init {
        generateTypescriptSDKFromSpringHandlers(
            requestMappingHandlerMapping,
            buildDirectory = "./ts-client/api-sdk"
        )
    }
}
