package kttsRPC.rpclib

import kttsRPC.types.HandlerMetadata
import kttsRPC.types.TypeName
import kttsRPC.utils.BuildTSFunctionHandlerMD
import kttsRPC.utils.ExtendedTypeInformation
import kttsRPC.utils.buildTSFunction
import java.io.File

fun writeSDKFile(
    handlersMD: List<HandlerMetadata>,
    userTypes: Set<TypeName>,
    outFilePath: String
) {
    val transformedHandlersMD: Map<String, List<BuildTSFunctionHandlerMD>> =
        handlersMD
            .groupBy { it.controllerName }
            .mapValues { (_, handlers) ->
                handlers.map { handler ->
                    BuildTSFunctionHandlerMD(
                        path = handler.path,
                        functionName = handler.functionName,
                        method = handler.method,
                        paramsType = handler.paramsType.ifEmpty { null },
                        queryStringType = handler.queryStringType.ifEmpty { null },
                        requestBodyType = if (handler.requestBodyType == null) {
                            null
                        } else {
                            ExtendedTypeInformation(
                                type = handler.requestBodyType,
                                isUserType = userTypes.contains(handler.requestBodyType.simpleName)
                            )
                        },
                        successResponseType = ExtendedTypeInformation(
                            type = handler.successResponseType,
                            isUserType = userTypes.contains(handler.successResponseType.simpleName)
                        ),
                        errorResponseType = if (handler.errorResponseType == null) null else
                            ExtendedTypeInformation(
                                type = handler.errorResponseType,
                                isUserType = userTypes.contains(handler.errorResponseType.simpleName)
                            )
                    )
                }
            }

    // Write to SDK file
    val sdkFileContents = StringBuilder()

    // Append imports
    sdkFileContents.appendLine("import UserTypes from \"./UserTypes\";")
    sdkFileContents.appendLine("import { HandlerResponse } from \"./HandlerResponse\";")
    sdkFileContents.appendLine("import { replacePathAndQueryVariables } from \"./Utils\";")
    sdkFileContents.appendLine()

    // Append SDK factory
    sdkFileContents.appendLine("export default function BuildSDK(apiBaseUrl: string) { return {")

    for ((controllerName, controllerHandlers) in transformedHandlersMD) {
        sdkFileContents.appendLine("\t${controllerName.lowercase()}: {")

        for (handler in controllerHandlers)
            sdkFileContents.appendLine("\t\t${handler.functionName}: ${buildTSFunction(handler)},")

        sdkFileContents.appendLine("\t},")
    }
    sdkFileContents.appendLine("}}")

    File(outFilePath)
        .writeText(sdkFileContents.toString())
}
