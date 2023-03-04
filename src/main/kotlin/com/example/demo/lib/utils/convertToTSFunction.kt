package com.example.demo.lib.utils

import com.example.demo.lib.types.HandlerMetadata

fun convertToTSFunction(
    handlerMD: HandlerMetadata
): String {
    val tsTypeGenerator = KotlinDataClassToTypescriptInterfaces()
    val stringBuilder = StringBuilder()

    val paramsTSType = if (handlerMD.paramsType.isEmpty())
        null // handler doesn't need params/path variables
    else
        "params: ${tsTypeGenerator.fromMap(handlerMD.paramsType).result},"

    val queryStringTSType = if (handlerMD.queryStringType.isEmpty())
        null // handler doesn't need query string variables
    else
        "query: ${tsTypeGenerator.fromMap(handlerMD.queryStringType).result},"

    val requestBodyTSType = if (handlerMD.requestBodyType == null)
        null // handler doesn't need request body
    else
        "body: UserTypes.${handlerMD.requestBodyType.simpleName},"

    // ! Watch out for ResponseEntity<Primitives>. Might already work but try it out
    val successResponseType = if (handlerMD.successResponseType == Unit::class)
        "void"
    else
        handlerMD.successResponseType.simpleName

    val errorResponseType = if (handlerMD.errorResponseType == Unit::class)
        "void"
    else
        handlerMD.errorResponseType.simpleName

    // Builds function signature
    stringBuilder.appendLine("async (args: { ")

    if (paramsTSType != null)
        stringBuilder.appendLine("\t${paramsTSType}")
    if (queryStringTSType != null)
        stringBuilder.appendLine("\t${queryStringTSType}")
    if (requestBodyTSType != null)
        stringBuilder.appendLine("\t${requestBodyTSType}")

    stringBuilder.appendLine(" }): Promise<ServerResponse<UserTypes.$successResponseType, UserTypes.$errorResponseType>> => {")

    // Builds function body
    if (successResponseType == "void" && errorResponseType == "void")
        stringBuilder.appendLine("\tawait fetch(")
    else
        stringBuilder.appendLine("\treturn fetch(")

    stringBuilder.appendLine(
        "\t\treplacePathAndQueryVariables(`\${apiBaseUrl}${handlerMD.path}`, " +
                "${if (paramsTSType != null) "args.params" else "undefined"}, " +
                (if (queryStringTSType != null) "args.query" else "undefined")
                + "),"
    )

    // Builds fetch request body
    if (requestBodyTSType != null) {
        stringBuilder.appendLine("\t\t{")
        stringBuilder.appendLine("\t\t\theaders: {\"Content-Type\": \"application/json\"},")
        stringBuilder.appendLine("\t\t\tbody: JSON.stringify(args.body)")
        stringBuilder.appendLine("\t\t}")
    }

    stringBuilder.appendLine("\t)")

    if (successResponseType != "void" || errorResponseType != "void")
        stringBuilder.appendLine("\t\t.then(res => res.json())")

    stringBuilder.appendLine("}")

    return stringBuilder.toString()
}