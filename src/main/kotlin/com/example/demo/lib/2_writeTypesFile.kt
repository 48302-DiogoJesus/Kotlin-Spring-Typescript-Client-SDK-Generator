package com.example.demo.lib

import com.example.demo.lib.types.HandlerMetadata
import com.example.demo.lib.utils.TSTypesGenerator
import java.io.File

fun writeTypesFile(handlersMetadata: List<HandlerMetadata>, typesFilePath: String) {
    val typesFile = File(typesFilePath)
        .also { it.writeText("") }

    val writtenTypes: MutableSet<String> = mutableSetOf()

    val tsTypeGenerator = TSTypesGenerator()
    handlersMetadata.forEach { handler ->
        if (handler.requestBodyType != null) {
            // Request body is needed for this handler
            val res = tsTypeGenerator.fromKClass(handler.requestBodyType, handler.requestBodyType.simpleName)
            // ! write res to types file
            res.typesCreated.forEach { writtenTypes.add(it) }
        }

        // Response is required, even if its Unit (ts void)
        // If it's Unit don't generate a type definition for it
        if (handler.successResponseType != Unit::class) {
            val res = tsTypeGenerator.fromKClass(handler.successResponseType, handler.successResponseType.simpleName)
            res.typesCreated.forEach { writtenTypes.add(it) }
        }

        if (handler.errorResponseType != Unit::class) {
            val res = tsTypeGenerator.fromKClass(handler.errorResponseType, handler.errorResponseType.simpleName)
            res.typesCreated.forEach { writtenTypes.add(it) }
        }
    }

    typesFile.appendText(writtenTypes.joinToString("\n"))
}