package com.example.demo.utils

import com.example.demo.types.HandlerMetadata

fun convertToTSFunction(
    handlerMD: HandlerMetadata
): String {
    val ktToTSMapConverter = KotlinDataClassToTypescriptInterfaces()::convertFromMapInline
    val stringBuilder = StringBuilder()

    val paramsTSType = if (handlerMD.paramsType.isEmpty())
        null // handler doesn't need params/path variables
    else
        "params: ${ktToTSMapConverter(handlerMD.paramsType)},"

    val queryStringTSType = if (handlerMD.queryStringType.isEmpty())
        null // handler doesn't need query string variables
    else
        "query: ${ktToTSMapConverter(handlerMD.queryStringType)},"

    val requestBodyTSType = if (handlerMD.requestBodyType == null)
        null// handler doesn't need request body
    else
        "body: ${handlerMD.requestBodyType.simpleName},"

    // ! Watch out for ResponseEntity<Primitives>. Might already work but try it out
    val responseBodyType = if (handlerMD.responseBodyType == Unit::class)
        "void"
    else
        handlerMD.responseBodyType.simpleName

    // Builds function signature
    stringBuilder.appendLine("async (args: { ")

    if (paramsTSType != null)
        stringBuilder.appendLine("\t${paramsTSType}")
    if (queryStringTSType != null)
        stringBuilder.appendLine("\t${queryStringTSType}")
    if (requestBodyTSType != null)
        stringBuilder.appendLine("\t${requestBodyTSType}")

    stringBuilder.appendLine(" }): Promise<$responseBodyType> => {")

    // Builds function body
    if (responseBodyType == "void")
        stringBuilder.appendLine("\tawait fetch(")
    else
        stringBuilder.appendLine("\treturn fetch(")

    stringBuilder.appendLine(
        "\t\treplacePathAndQueryVariables(\"HARDCODED_BASE_URL${handlerMD.path}\", " +
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

    if (responseBodyType != "void")
        stringBuilder.appendLine("\t\t.then(res => res.json())")

    stringBuilder.appendLine("}")

    return stringBuilder.toString()
}