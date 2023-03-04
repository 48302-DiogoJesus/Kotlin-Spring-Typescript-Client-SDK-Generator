package com.example.demo.lib.utils

import com.example.demo.lib.types.TypeDetails
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
    private val typesCreated = mutableMapOf<String, String>()

    data class ConversionResult(
        val result: String,
        val typesCreated: List<String>
    )

    /**

     * Currently used for 1 level maps: + params/path variables
     * */
    fun fromMap(
        map: Map<String, TypeDetails>,
        typeName: String? = null
    ): ConversionResult {
        val properties = map.entries.joinToString(",\n") { (k, v) ->
            "\t${k}${if (!v.required) "?" else ""}: ${convertKotlinTypeToTypeScript(v.type.createType())}"
        }
        val typesCopy = typesCreated.toMap().values.toList()
        typesCreated.clear()

        return ConversionResult(
            if (typeName == null)
                "{$properties}"
            else
                "export interface $typeName {$properties}",
            typesCopy
        )
    }

    fun fromKClass(klass: KClass<*>, typeName: String? = null): ConversionResult {
        val result = convertInternal(klass, typeName)

        val typesCopy = HashMap(typesCreated).values.toList()
        typesCreated.clear()
        return ConversionResult(result, typesCopy)
    }

    private fun convertInternal(
        kotlinClass: KClass<*>,
        finalInterfaceName: String? = null,
    ): String {
        val className = kotlinClass.simpleName
            ?: finalInterfaceName
            ?: throw Error("Class with no name tried to be converted")

        val cachedType = typesCreated[className]
        if (cachedType != null)
            return cachedType

        val properties = kotlinClass.memberProperties.joinToString(",\n") {
            val propertyType = it.returnType
            val propertyTypeName =
                "${convertKotlinTypeToTypeScript(propertyType)} ${
                    if (propertyType.isMarkedNullable) "| null" else ""
                }"
            "\t${it.name}: $propertyTypeName"
        }
        val builtType = "export interface $className {\n$properties\n}"
        typesCreated[className] = builtType
        return builtType
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
                "{ [key: ${convertKotlinTypeToTypeScript(keyType)}]: ${
                    convertKotlinTypeToTypeScript(
                        valueType
                    )
                } }"
            }

            else -> {
                val nestedClass = (type.classifier as? KClass<*>) ?: return "any"
                val nestedClassName = nestedClass.simpleName ?: return "any"
                // Will create an interface for another type
                convertInternal(nestedClass).replace(nestedClass.qualifiedName!!, nestedClassName)
                nestedClassName
            }
        }
    }
}

