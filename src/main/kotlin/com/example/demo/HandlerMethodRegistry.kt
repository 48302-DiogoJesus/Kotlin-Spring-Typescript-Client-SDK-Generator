package com.example.demo

import com.example.demo.lib.*
import org.springframework.stereotype.Component
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping
import kotlin.system.exitProcess

/**
 * Library clients would have to somehow get the RequestMappingInfoHandlerMapping to call my generator:
 * generateTypescriptSDKFromSpringHandlers
 */
@Component
class TypescriptSDKGenerator(requestMappingHandlerMapping: RequestMappingInfoHandlerMapping) {
    // * Remove when used as jar library
    init {
        generateTypescriptSDKFromSpringHandlers(requestMappingHandlerMapping)
    }
}






