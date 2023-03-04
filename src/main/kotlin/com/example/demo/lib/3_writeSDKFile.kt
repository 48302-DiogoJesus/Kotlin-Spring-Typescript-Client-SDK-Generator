package com.example.demo.lib

import com.example.demo.lib.types.HandlerMetadata
import com.example.demo.lib.types.TypeName
import com.example.demo.lib.utils.ConvertToTSFunctionHandler
import com.example.demo.lib.utils.TypeInformation
import com.example.demo.lib.utils.convertToTSFunction
import java.io.File

fun writeSDKFile(
    handlersMD: List<HandlerMetadata>,
    userTypes: Set<TypeName>,
    outFilePath: String
) {
    val transformedHandlersMD: Map<String, List<ConvertToTSFunctionHandler>> =
        handlersMD
            .groupBy { it.controllerName }
            .mapValues { (_, handlers) ->
                handlers.map { handler ->
                    ConvertToTSFunctionHandler(
                        path = handler.path,
                        functionName = handler.functionName,
                        paramsType = handler.paramsType.ifEmpty { null },
                        queryStringType = handler.queryStringType.ifEmpty { null },
                        requestBodyType = if (handler.requestBodyType == null)
                            null
                        else
                            TypeInformation(
                                type = handler.requestBodyType,
                                isUserType = userTypes.contains(handler.requestBodyType.simpleName)
                            ),
                        successResponseType = TypeInformation(
                            type = handler.successResponseType,
                            isUserType = userTypes.contains(handler.successResponseType.simpleName)
                        ),
                        errorResponseType = TypeInformation(
                            type = handler.errorResponseType,
                            isUserType = userTypes.contains(handler.errorResponseType.simpleName)
                        ),
                    )
                }
            }

    // Write to SDK file
    val sdkFileContents = StringBuilder()

    // Append imports
    sdkFileContents.appendLine("import UserTypes from \"./UserTypes\";")
    sdkFileContents.appendLine("import { ServerResponse } from \"./ServerResponse\";")
    sdkFileContents.appendLine("import { replacePathAndQueryVariables } from \"./Utils\";")
    sdkFileContents.appendLine()

    // Append SDK factory
    sdkFileContents.appendLine("export default function BuildSDK(apiBaseUrl: string) { return {")

    for ((controllerName, controllerHandlers) in transformedHandlersMD) {
        sdkFileContents.appendLine("\t${controllerName}: {")

        for (handler in controllerHandlers)
            sdkFileContents.appendLine("\t\t${handler.functionName}: ${convertToTSFunction(handler)},")

        sdkFileContents.appendLine("\t},")
    }
    sdkFileContents.appendLine("}}")

    File(outFilePath)
        .writeText(sdkFileContents.toString())
}