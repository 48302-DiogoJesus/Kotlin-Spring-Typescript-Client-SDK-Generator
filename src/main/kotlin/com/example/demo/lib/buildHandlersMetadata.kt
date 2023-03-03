package com.example.demo.lib

import com.example.demo.types.HandlerMetadata
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass

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

        return@mapNotNull HandlerMetadata(
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
