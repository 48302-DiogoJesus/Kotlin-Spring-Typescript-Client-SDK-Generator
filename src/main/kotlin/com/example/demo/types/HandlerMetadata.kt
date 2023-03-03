package com.example.demo.types

import org.springframework.web.bind.annotation.RequestMethod
import kotlin.reflect.KClass

data class HandlerMetadata(
    val controllerName: String,
    val functionName: String,

    val method: RequestMethod,
    val path: String,

    val paramsType: Map<String, KClass<*>>,
    val queryStringType: Map<String, KClass<*>>,
    val requestBodyType: KClass<*>?,
    val responseBodyType: KClass<*>
)