package com.example.demo.types

import org.springframework.web.bind.annotation.RequestMethod
import kotlin.reflect.KClass

// Used only for query string and params/path variables
data class TypeDetails(
    val type: KClass<*>,
    val required: Boolean
)

data class HandlerMetadata(
    val controllerName: String,
    val functionName: String,

    val method: RequestMethod,
    val path: String,

    val paramsType: Map<String, TypeDetails>,
    val queryStringType: Map<String, TypeDetails>,
    val requestBodyType: KClass<*>?,
    val responseBodyType: KClass<*>
)