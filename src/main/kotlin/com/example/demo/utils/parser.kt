package com.example.demo.utils

import java.io.File
import java.sql.Date
import java.sql.Timestamp
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties

/**
 * Something that can:
 * - convert kotlin data classes to TS interfaces
 * - convert kotlin Map to TS object type definition
 * */
class KotlinDataClassToTypescriptInterfaces() {
    fun convertFromMapInline(map: Map<String, KClass<*>>): String {
        val properties = map.entries.joinToString(",\n") { (k, v) ->
            "\t${k}: ${convertKotlinTypeToTypeScript(v.createType())}"
        }
        return "{$properties}"
    }

    fun useConvert(
        outputFilePath: String,
        block: (convert: (KClass<*>, String?) -> String) -> Unit
    ) {
        block(::convert)
        writeToFile(outputFilePath)
    }

    private val cachedTypes = mutableMapOf<String, String>()

    private fun convert(kotlinClass: KClass<*>, finalInterfaceName: String? = null): String {
        val className = kotlinClass.simpleName
            ?: finalInterfaceName
            ?: throw Error("Class with no name tried to be converted")

        val cachedType = cachedTypes[className]
        if (cachedType != null)
            return cachedType

        val properties = kotlinClass.memberProperties.joinToString(",\n") {
            val propertyType = it.returnType
            val propertyTypeName =
                "${convertKotlinTypeToTypeScript(propertyType)} ${if (propertyType.isMarkedNullable) "| null" else ""}"
            "\t${it.name}: $propertyTypeName"
        }
        val builtType = "export interface $className {\n$properties\n}"
        cachedTypes[className] = builtType
        return builtType
    }

    fun writeToFile(filePath: String) {
        val file = File(filePath)
        file.writeText(cachedTypes.values.joinToString("\n"))
        cachedTypes.clear()
    }

    private fun convertKotlinTypeToTypeScript(type: KType): String {
        return when (type.classifier) {
            String::class -> "string"
            Int::class -> "number"
            Long::class -> "number"
            Double::class -> "number"
            Boolean::class -> "boolean"
            Date::class -> "Date"
            Timestamp::class -> "Date"
            List::class -> {
                val nestedType = type.arguments.firstOrNull()?.type ?: return "any[]"
                if (nestedType.isMarkedNullable)
                    "(${convertKotlinTypeToTypeScript(nestedType)} | null)[]"
                else
                    "${convertKotlinTypeToTypeScript(nestedType)}[]"
            }

            Map::class -> {
                val keyType = type.arguments.firstOrNull()?.type ?: return "{ [key: string]: any }"
                val valueType = type.arguments.getOrNull(1)?.type ?: return "{ [key: string]: any }"
                "{ [key: ${convertKotlinTypeToTypeScript(keyType)}]: ${convertKotlinTypeToTypeScript(valueType)} }"
            }

            else -> {
                val nestedClass = (type.classifier as? KClass<*>) ?: return "any"
                val nestedClassName = nestedClass.simpleName ?: return "any"
                // Will create an interface for another type
                convert(nestedClass).replace(nestedClass.qualifiedName!!, nestedClassName)
                nestedClassName
            }
        }
    }
}

