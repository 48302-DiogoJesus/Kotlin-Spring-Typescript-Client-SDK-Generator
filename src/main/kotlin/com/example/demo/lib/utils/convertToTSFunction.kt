package com.example.demo.lib.utils

import org.springframework.web.bind.annotation.RequestMethod
import kotlin.reflect.KClass
import kotlin.reflect.KType

data class TypeInformation(
    val type: KClass<*>,
    val isUserType: Boolean
)

data class ConvertToTSFunctionHandler(
    val path: String,
    val functionName: String,
    val method: RequestMethod,

    val paramsType: Map<String, KType>?,
    val queryStringType: Map<String, KType>?,

    val requestBodyType: TypeInformation?,

    val successResponseType: TypeInformation,
    val errorResponseType: TypeInformation
)

/**
 * ! REMOVE COMMENT LATER
 * - is it a user type? request, response, error
 * - ts types already created in the previous step for NAMED types
 * */
fun convertToTSFunction(
    handlerMD: ConvertToTSFunctionHandler,
): String {
    val tsTypeGenerator = TSTypesGenerator()
    val stringBuilder = StringBuilder()

    // function arguments
    stringBuilder.appendLine("(args: { ")

    if (handlerMD.paramsType != null)
        stringBuilder.appendLine("\tparams: ${tsTypeGenerator.fromMap(handlerMD.paramsType).result},")

    if (handlerMD.queryStringType != null)
        stringBuilder.appendLine("\tquery: ${tsTypeGenerator.fromMap(handlerMD.queryStringType).result},")

    if (handlerMD.requestBodyType != null) {
        val toAppend = if (handlerMD.requestBodyType.isUserType)
            "body: UserTypes.${handlerMD.requestBodyType.type.simpleName},"
        else
            "body: ${tsTypeGenerator.fromKClass(handlerMD.requestBodyType.type).result},"

        stringBuilder.appendLine("\t${toAppend}")
    }

    // return type
    val successResponseType = if (handlerMD.successResponseType.type == Unit::class)
        "void"
    else {
        if (handlerMD.successResponseType.isUserType)
            "UserTypes.${handlerMD.successResponseType.type.simpleName}"
        else
            tsTypeGenerator.fromKClass(handlerMD.successResponseType.type).result
    }

    val errorResponseType = if (handlerMD.errorResponseType.type == Unit::class)
        "void"
    else {
        if (handlerMD.errorResponseType.isUserType)
            "UserTypes.${handlerMD.errorResponseType.type.simpleName}"
        else
            tsTypeGenerator.fromKClass(handlerMD.errorResponseType.type).result
    }
    stringBuilder.appendLine(" }): Promise<ServerResponse<$successResponseType, $errorResponseType>> => ")

    // Builds function body
    stringBuilder.appendLine("\tfetch(")
    stringBuilder.appendLine(
        "\t\treplacePathAndQueryVariables(`\${apiBaseUrl}${handlerMD.path}`, " +
                "${if (handlerMD.paramsType != null) "args.params" else "undefined"}, " +
                (if (handlerMD.queryStringType != null) "args.query" else "undefined")
                + "),"
    )

    stringBuilder.appendLine("\t\t{")

    stringBuilder.appendLine("\t\t\tmethod: \"${handlerMD.method}\",")

    if (handlerMD.requestBodyType != null) {
        // Builds request body if it exists
        stringBuilder.appendLine("\t\t\theaders: {\"Content-Type\": \"application/json\"},")
        stringBuilder.appendLine("\t\t\tbody: JSON.stringify(args.body)")
    }

    stringBuilder.appendLine("\t\t}")

    stringBuilder.appendLine("\t)")
    stringBuilder.appendLine("\t\t.then(res => res.json())")

    return stringBuilder.toString()
}