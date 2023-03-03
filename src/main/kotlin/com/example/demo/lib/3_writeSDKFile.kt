package com.example.demo.lib

import com.example.demo.types.HandlerMetadata
import com.example.demo.utils.convertToTSFunction
import java.io.File

fun writeSDKFile(handlersMetadata: List<HandlerMetadata>, outFilePath: String) {
    val sb = StringBuilder()
    sb.appendLine("const sdk = {")

    val controllers: Map<String, List<HandlerMetadata>> =
        handlersMetadata.groupBy { it.controllerName }

    for (controller in controllers) {
        sb.appendLine("\t${controller.key}: {")
        val controllerHandlers = controller.value
        for (handler in controllerHandlers) {
            sb.appendLine("\t\t${handler.functionName}: ${convertToTSFunction(handler)},")
        }
        sb.appendLine("\t},")
    }
    sb.appendLine("}")
    sb.appendLine()
    sb.append("export default sdk")

    // Write to SDK file
    val sdk = File(outFilePath)
    sdk.writeText("import { RequestData, ResponseData } from \"./types\";\n")
    sdk.appendText("import { replacePathAndQueryVariables } from \"./utils\";\n")
    sdk.appendText("\n")
    sdk.appendText(sb.toString())
}