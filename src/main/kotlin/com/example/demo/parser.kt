package com.example.demo

import java.io.File
import java.sql.Date
import java.sql.Timestamp
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties

class KotlinDataClassToTypescriptInterfaces(
    val outputFilePath: String?
) {
    val typesToWrite = mutableMapOf<String, String>()

    fun convertFromMapInline(map: Map<String, KClass<*>>): String {
        val properties = map.values.joinToString(",\n") {
            "\t${it.simpleName}: ${convert(it)}"
        }
        return "{$properties}"
    }

    fun convert(kotlinClass: KClass<*>, finalInterfaceName: String? = null): String {
        val className = kotlinClass.simpleName
            ?: finalInterfaceName
            ?: throw Error("Class with no name tried to be converted")

        val cachedType = typesToWrite[className]
        if (cachedType != null)
            return cachedType

        val properties = kotlinClass.memberProperties.joinToString(",\n") {
            val propertyType = it.returnType
            val propertyTypeName =
                "${convertKotlinTypeToTypeScript(propertyType)} ${if (propertyType.isMarkedNullable) "| null" else ""}"
            "\t${it.name}: $propertyTypeName"
        }
        val builtType = "interface $className {\n$properties\n}"
        typesToWrite[className] = builtType
        return builtType
    }

    fun write() {
        if (outputFilePath == null)
            throw Error("No file specified to write to")

        val file = File(outputFilePath)
        file.writeText(typesToWrite.values.joinToString())
        typesToWrite.clear()
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

