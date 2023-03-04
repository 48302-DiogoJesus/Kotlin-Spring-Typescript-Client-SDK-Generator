package com.example.demo.lib

import java.io.File

fun writeDependencies(
    utilsFilePath: String,
    serverResponseTypeFilePath: String
) {
    File(utilsFilePath).writeText(
        "export function replacePathAndQueryVariables(\n" +
                "  url: string,\n" +
                "  variables?: { [key: string]: any },\n" +
                "  query?: { [key: string]: any },\n" +
                "): string {\n" +
                "  let result = url;\n" +
                "  if (variables) {\n" +
                "    for (const key in variables) {\n" +
                "      if (Object.prototype.hasOwnProperty.call(variables, key)) {\n" +
                "        result = result.replace(`{\${key}}`, variables[key]);\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "\n" +
                "  if (query) {\n" +
                "    result += \"?\";\n" +
                "    Object.entries(query).forEach(([k, v]) => {\n" +
                "      result += `\${k}=\${v}&`;\n" +
                "    });\n" +
                "  }\n" +
                "\n" +
                "  return result;\n" +
                "}"
    )

    File(serverResponseTypeFilePath).writeText(
        "type SuccessResponse<S> = {\n" +
                "  isSuccess: true;\n" +
                "  data: S;\n" +
                "};\n" +
                "\n" +
                "type ErrorResponse<E> = {\n" +
                "  isSuccess: false;\n" +
                "  error: E;\n" +
                "};\n" +
                "\n" +
                "export type ServerResponse<S, E> = SuccessResponse<S> | ErrorResponse<E>;\n"
    )
}