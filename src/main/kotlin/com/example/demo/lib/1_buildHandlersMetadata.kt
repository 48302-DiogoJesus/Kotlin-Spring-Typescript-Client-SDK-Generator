package com.example.demo.lib

import com.example.demo.lib.types.HandlerMetadata
import org.jetbrains.annotations.NotNull
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping
import java.lang.reflect.Parameter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType

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

        val paramsTypes = mutableMapOf<String, KType>()
        handler.method.parameters
            .mapNotNull { it.getAnnotation(PathVariable::class.java)?.let { annotation -> it to annotation } }
            .forEach { (param, annotation) ->
                paramsTypes[annotation.name] = param.type.kotlin.createType(nullable = !annotation.required)
            }

        val queryTypes = mutableMapOf<String, KType>()
        handler.method.parameters
            .mapNotNull { it.getAnnotation(RequestParam::class.java)?.let { annotation -> it to annotation } }
            .forEach { (param, annotation) ->
                queryTypes[annotation.name] = param.type.kotlin.createType(nullable = !annotation.required);
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
