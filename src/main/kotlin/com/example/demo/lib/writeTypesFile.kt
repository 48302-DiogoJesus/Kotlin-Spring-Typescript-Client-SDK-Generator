package com.example.demo.lib

import com.example.demo.types.HandlerMetadata
import com.example.demo.utils.KotlinDataClassToTypescriptInterfaces

fun writeTypesFile(handlersMetadata: List<HandlerMetadata>, outFilePath: String) {
    KotlinDataClassToTypescriptInterfaces()
        .useConvert(outFilePath) { convert ->
            handlersMetadata.forEach { handler ->
                println(handler)

                if (handler.requestBodyType != null) {
                    // Request body is needed for this handler
                    convert(handler.requestBodyType, handler.requestBodyType.simpleName)
                }

                // Response is required, even if its Unit (ts void)
                // If it's Unit don't generate a type definition for it
                if (handler.responseBodyType != Unit::class)
                    convert(handler.responseBodyType, handler.responseBodyType.simpleName)
            }
        }
}