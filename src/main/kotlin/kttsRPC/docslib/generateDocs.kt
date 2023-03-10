package kttsRPC.docslib

import kttsRPC.rpclib.buildHandlersMetadata
import kttsRPC.types.TypeName
import kttsRPC.utils.TSTypesGenerator
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping
import java.io.File

data class HandlerMetadataWithTypes(
    val controllerName: String,
    val functionName: String,
    val method: RequestMethod,
    val path: String,
    val pathVars: String?,
    val queryStringType: String?,
    val requestBodyType: String?,
    val successResponseType: String,
    val errorResponseType: String?
)

fun generateDocs(
    requestMappingHandlerMapping: RequestMappingInfoHandlerMapping,
    buildDirectory: String
) {
    val handlersMetadata =
        buildHandlersMetadata(requestMappingHandlerMapping)

    val typesGen = TSTypesGenerator()

    handlersMetadata.map {
        val requestBodyType = if (it.requestBodyType != null)
            typesGen.fromKClass(it.requestBodyType)
        else null
        val successType = typesGen.fromKClass(it.successResponseType)
        val errorType = if (it.errorResponseType != null)
            typesGen.fromKClass(it.errorResponseType)
        else null

        // Anonymous types
        val paramsType = if (it.paramsType.isEmpty()) null else typesGen.fromMap(it.paramsType)
        val queryStringType = if (it.queryStringType.isEmpty()) null else typesGen.fromMap(it.queryStringType)

        val mergedTypesForHandler: Map<TypeName, String> = (
                (requestBodyType?.typesCreated ?: mapOf()) +
                        (paramsType?.typesCreated ?: mapOf()) +
                        (queryStringType?.typesCreated ?: mapOf()) +
                        successType.typesCreated +
                        (errorType?.typesCreated ?: mapOf())
                ).mapValues { e -> e.value.removePrefix("export interface ") }

        return@map Pair(
            HandlerMetadataWithTypes(
                it.controllerName, it.functionName, it.method, it.path,
                paramsType?.result?.removePrefix("export interface "),
                queryStringType?.result?.removePrefix("export interface "),
                requestBodyType?.result?.removePrefix("export interface "),
                successType.result.removePrefix("export interface "),
                errorType?.result?.removePrefix("export interface ")
            ),
            mergedTypesForHandler
        )
    }
        .let {
            val typesToWrite = it.map { p -> p.second }.flatMap { fp -> fp.entries }
                .fold(mutableMapOf<TypeName, String>()) { acc, e ->
                    acc[e.key] = e.value
                    acc
                }
                .values
                .joinToString("\n\n") { typeStr -> "```json\n${typeStr}\n```" }

            // Create build directory if not exists
            File(buildDirectory).mkdir()

            // Write all the involved named types to a file
            File("${buildDirectory}/api-types.md")
                .writeText(typesToWrite)

            return@let it.map { p -> p.first }
        }
        .groupBy { it.controllerName }
        .also {
            val outputMD = StringBuilder()

            val errorResponseType = it.values.flatten().find { h -> h.errorResponseType != null }?.errorResponseType
                ?: throw Error("No Error Type Found")

            outputMD.appendLine("## Global Error Format\n")
            outputMD.appendLine("```json\n${errorResponseType}\n```")

            // Build markdown docs
            for ((controllerName, controllerHandlers) in it) {
                outputMD.appendLine("---\n")
                outputMD.appendLine("### $controllerName\n")

                for (handler in controllerHandlers) {
                    outputMD.appendLine("#### ${handler.method} ${handler.path}\n")
                    if (handler.queryStringType != null) {
                        outputMD.appendLine("`Query String`\n")
                        outputMD.appendLine("```json\n${handler.queryStringType}\n```")
                    }
                    // Put path variables inline with {path}
                    if (handler.pathVars != null) {
                        outputMD.appendLine("`Path Variables`\n")
                        outputMD.appendLine("```json\n${handler.pathVars}\n```")
                    }
                    if (handler.requestBodyType != null) {
                        outputMD.appendLine("`Request Body`\n")
                        outputMD.appendLine("```json\n${handler.requestBodyType}\n```")
                    }
                    outputMD.appendLine("`Response Body`\n")
                    outputMD.appendLine("```json\n${handler.successResponseType}\n```")
                }
                outputMD.appendLine("\n")
            }

            File("${buildDirectory}/api-docs.md")
                .writeText(outputMD.toString())

            println("[API Documentation Generator] DONE | Output at: $buildDirectory")
        }
}