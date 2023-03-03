package com.example.demo

import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping
import java.io.File
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

/**
 * Extract from handler:
 * - method
 * - full path
 * - request body, params and query types
 * - response body type
 *
 * function get({
 *
 *  path: "",
 *  params: {key: value}
 *  query: {key: value}
 *  body: {key: value}
 *
 * }): return type (data class converted to typescript type) =
 *  await fetch(hostname + path, {
 *      2
 *  })
 *
 *  const api = {
 *      controllerName: {
 *          handlerName: {
 *              get: (data: { id: string }) =>
 *                  fetch(baseurl + path, {
 *                      headers:{
 *                          'Content-Type': 'application/json'
 *                      },
 *                      body:
 *                  })
 *          }
 *       },
 *  }
 * */

fun buildTSFunctionFromHandlerMetadata(
    handlerMetadata: HandlerMethodMetadata,
    functionOutputPath: File,
    functionTypesPath: File? = functionOutputPath
) {
    // Clear
    functionOutputPath.writeText("")
    val stringBuilder = StringBuilder()
    val kotlinToTsTypes = KotlinDataClassToTypescriptInterfaces(null)

    val paramsTSType = kotlinToTsTypes.convertFromMapInline(handlerMetadata.paramsType)
    val queryStringTSType = kotlinToTsTypes.convertFromMapInline(handlerMetadata.queryStringType)
    val requestBodyTSType = if (handlerMetadata.requestBodyType != null)
        handlerMetadata.requestBodyType.simpleName
    else
        "{}"
    // ! Watch out for ResponseEntity<Unit>
    val responseBodyType = handlerMetadata.responseBodyType.simpleName

    // Builds function signature
    stringBuilder.append(
        "(args: { " +
                "p: $paramsTSType" +
                "q: $queryStringTSType" +
                "b: $requestBodyTSType " +
            " }): $responseBodyType"
    )
    stringBuilder.append(" => fetch()")
}

data class HandlerMethodMetadata(
    val controllerName: String,
    val functionName: String,
    val method: RequestMethod,
    val path: String,
    val paramsType: Map<String, KClass<*>>,
    val queryStringType: Map<String, KClass<*>>,
    val requestBodyType: KClass<*>?,
    val responseBodyType: KClass<*>
)

@Component
class HandlerMethodRegistry(
    val requestMappingHandlerMapping: RequestMappingInfoHandlerMapping
) {
    init {
        val handlersMetadata = buildHandlersMetadata()
        val kotlinToTsTypes = KotlinDataClassToTypescriptInterfaces("types.d.ts")

        /**
         * Create types that will be then used by the functions
         * Types from:
         *  query, params, body, request body, response body
         * */
        handlersMetadata.forEach { hm ->
            hm.paramsType.values.forEach { kotlinToTsTypes.convert(it.kotlin) }
            hm.queryStringType.values.forEach { kotlinToTsTypes.convert(it.kotlin) }

            if (hm.requestBodyType != null)
                kotlinToTsTypes.convert(hm.requestBodyType.kotlin)

            kotlinToTsTypes.convert(hm.responseBodyType.kotlin)
        }
        kotlinToTsTypes.write()
    }

    fun buildHandlersMetadata(): List<HandlerMethodMetadata> =
        requestMappingHandlerMapping.handlerMethods.entries.mapNotNull { (reqInfo, handler) ->
            val controllerName = handler.beanType.simpleName
                ?: return@mapNotNull null
            val functionName = handler.method.name
                ?: return@mapNotNull null
            val path = reqInfo.pathPatternsCondition?.firstPattern?.patternString
                ?: return@mapNotNull null
            val method = reqInfo.methodsCondition.methods.firstOrNull()
                ?: return@mapNotNull null
            val requestBodyType =
                handler.method.parameters.firstOrNull { it.isAnnotationPresent(RequestBody::class.java) }?.type?.kotlin

            val paramsTypes = mutableMapOf<String, KClass<*>>()
            handler.method.parameters
                .filter { it.isAnnotationPresent(PathVariable::class.java) }
                .forEach { param -> paramsTypes[param.name] = param.type.kotlin }

            val queryTypes = mutableMapOf<String, KClass<*>>()
            handler.method.parameters
                .filter { it.isAnnotationPresent(RequestParam::class.java) }
                .forEach { param -> queryTypes[param.name] = param.type.kotlin }

            var responseTypeString: String = handler.method.returnType.typeName
                ?: return@mapNotNull null

            if (responseTypeString.contains("ResponseEntity"))
                responseTypeString =
                    (handler.method.genericReturnType as ParameterizedType).actualTypeArguments[0].typeName

            val responseBodyType: KClass<*> = Class.forName(responseTypeString).kotlin

            return@mapNotNull HandlerMethodMetadata(
                controllerName,
                functionName,
                method,
                path,
                paramsTypes,
                queryTypes,
                requestBodyType,
                responseBodyType
            )
        }
}




