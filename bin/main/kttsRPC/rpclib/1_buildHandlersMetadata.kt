package kttsRPC.rpclib

import kttsRPC.types.HandlerMetadata
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping
import java.lang.reflect.ParameterizedType
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
                paramsTypes[annotation.name.ifEmpty { param.name }] =
                    param.type.kotlin.createType(nullable = !annotation.required)
            }

        val queryTypes = mutableMapOf<String, KType>()
        handler.method.parameters
            .mapNotNull { it.getAnnotation(RequestParam::class.java)?.let { annotation -> it to annotation } }
            .forEach { (param, annotation) ->
                queryTypes[annotation.name.ifEmpty { param.name }] =
                    param.type.kotlin.createType(nullable = !annotation.required)
            }

        val completeResponseType: String = handler.method.returnType.typeName
            ?: return@mapNotNull null

        if (!completeResponseType.contains("ResponseEntity")) {
            throw Error("Your handlers should return ResponseEntity<YourResponseBodyType>")
        }

        val bodyType: String = (handler.method.genericReturnType as ParameterizedType).actualTypeArguments[0].typeName
            ?: throw Error("Your handlers should return ResponseEntity<YourResponseBodyType>")

        // SomeResponseBody<Success, Error>
        val successAndErrorResponses =
            "<(.+?), (.+?)>".toRegex()
                .find(bodyType)

        var successResponseType: KClass<*>? = null
        var errorResponseType: KClass<*>? = null

        if (successAndErrorResponses != null) {
            successResponseType = Class.forName(successAndErrorResponses.groupValues[1]).kotlin
            errorResponseType = Class.forName(successAndErrorResponses.groupValues[2]).kotlin
        } else {
            successResponseType = Class.forName(bodyType).kotlin
        }

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
