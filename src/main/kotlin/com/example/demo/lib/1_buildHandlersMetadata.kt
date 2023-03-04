package com.example.demo.lib

import com.example.demo.lib.types.HandlerMetadata
import com.example.demo.lib.types.TypeDetails
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping
import java.lang.reflect.ParameterizedType

fun buildHandlersMetadata(
    requestMappingHandlerMapping: RequestMappingInfoHandlerMapping
): List<HandlerMetadata> =
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

        val paramsTypes = mutableMapOf<String, TypeDetails>()
        handler.method.parameters
            .filter { it.isAnnotationPresent(PathVariable::class.java) }
            .forEach { param ->
                val annotationDetail = param.getAnnotation(PathVariable::class.java)
                paramsTypes[param.name] = TypeDetails(param.type.kotlin, annotationDetail.required)
            }

        val queryTypes = mutableMapOf<String, TypeDetails>()
        handler.method.parameters
            .filter { it.isAnnotationPresent(RequestParam::class.java) }
            .forEach { param ->
                val annotationDetail = param.getAnnotation(RequestParam::class.java)
                queryTypes[param.name] = TypeDetails(param.type.kotlin, annotationDetail.required)
            }

        val completeResponseType: String = handler.method.returnType.typeName
            ?: return@mapNotNull null

        if (!completeResponseType.contains("ResponseEntity")) {
            throw Error("Your handlers should return ResponseEntity<HandlerResponse<{SuccessBodyType}, {ErrorBodyType}>>")
        }

        val matchResult =
            "<(.+?), (.+?)>".toRegex()
                .find((handler.method.genericReturnType as ParameterizedType).actualTypeArguments[0].typeName)
                ?: throw Error("Your handlers should return ResponseEntity<HandlerResponse<{SuccessBodyType}, {ErrorBodyType}>>")

        val successResponseType = Class.forName(matchResult.groupValues[1]).kotlin
        val errorResponseType = Class.forName(matchResult.groupValues[2]).kotlin

        return@mapNotNull HandlerMetadata(
            controllerName,
            functionName,
            method,
            path,
            paramsTypes,
            queryTypes,
            requestBodyType,
            successResponseType,
            errorResponseType
        )
    }
