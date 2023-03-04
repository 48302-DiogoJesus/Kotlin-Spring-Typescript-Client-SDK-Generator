package com.example.demo.lib

import com.example.demo.lib.types.HandlerMetadata
import com.example.demo.lib.utils.convertToTSFunction
import java.io.File

fun writeSDKFile(handlersMetadata: List<HandlerMetadata>, outFilePath: String) {
    // Write to SDK file
    val sdkFileContents = StringBuilder()

    // Append imports
    sdkFileContents.appendLine("import UserTypes from \"./UserTypes\";")
    sdkFileContents.appendLine("import { ServerResponse } from \"./ServerResponse\";")
    sdkFileContents.appendLine("import { replacePathAndQueryVariables } from \"./Utils\";")
    sdkFileContents.appendLine()

    // Append SDK factory
    sdkFileContents.appendLine("export default function BuildSDK(apiBaseUrl: string) { return {")

    val controllers: Map<String, List<HandlerMetadata>> =
        handlersMetadata.groupBy { it.controllerName }

    for (controller in controllers) {
        sdkFileContents.appendLine("\t${controller.key}: {")
        val controllerHandlers = controller.value
        for (handler in controllerHandlers) {
            sdkFileContents.appendLine("\t\t${handler.functionName}: ${convertToTSFunction(handler)},")
        }
        sdkFileContents.appendLine("\t},")
    }
    sdkFileContents.appendLine("}}")

    File(outFilePath)
        .writeText(sdkFileContents.toString())
}