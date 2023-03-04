package com.example.demo.lib.types

import org.springframework.web.bind.annotation.RequestMethod
import kotlin.reflect.KClass
import kotlin.reflect.KType

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

    val paramsType: Map<String, KType>,
    val queryStringType: Map<String, KType>,
    val requestBodyType: KClass<*>?,

    val successResponseType: KClass<*>,
    val errorResponseType: KClass<*>
)