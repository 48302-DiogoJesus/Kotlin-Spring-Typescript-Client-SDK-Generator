package com.example.demo.lib.utils

import com.example.demo.lib.types.TypeName
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties

/**
 * Something that can:
 * - convert kotlin data classes to TS interfaces
 * - convert kotlin Map to TS object type definition
 * */
class TSTypesGenerator() {
    private val typesCreated = mutableMapOf<String, String>()

    data class ConversionResult(
        val result: String,
        val typesCreated: Map<TypeName, String>
    )

    /**
     * Currently used for 1 level maps: + params/path variables
     * */
    fun fromMap(
        map: Map<String, KType>,
        typeName: String? = null
    ): ConversionResult {
        val properties = map.entries.joinToString(",\n") { (propName, type) ->
            "\t${propName}${if (type.isMarkedNullable) "?" else ""}: ${convertKTypeToTSType(type)}"
        }
        val typesCopy = typesCreated.toMap()
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
        val result = if (!isUserType(klass))
            convertKTypeToTSType(klass.createType())
        else
            convertKClassInternal(klass, typeName)

        val typesCopy = typesCreated.toMap()
        typesCreated.clear()
        return ConversionResult(result, typesCopy)
    }

    private fun convertKClassInternal(
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
                "${convertKTypeToTSType(propertyType)} ${
                    if (propertyType.isMarkedNullable) "| null" else ""
                }"

            "\t${it.name}${if (propertyType.isMarkedNullable) "?" else ""}: $propertyTypeName"
        }
        val builtType = "export interface $className {\n$properties\n}"
        typesCreated[className] = builtType
        return builtType
    }

    private fun convertKTypeToTSType(type: KType): String {
        return when (type.classifier) {
            String::class -> "string"
            // ! Conversion not tested
            Timestamp::class -> "Date"
            Date::class -> "Date"
            Char::class -> "string"
            Int::class -> "number"
            Long::class -> "number"
            Float::class -> "number"
            Double::class -> "number"
            Byte::class -> "number"
            Boolean::class -> "boolean"
            List::class -> {
                val nestedType = type.arguments.firstOrNull()?.type ?: return "any[]"
                if (nestedType.isMarkedNullable)
                    "(${convertKTypeToTSType(nestedType)} | null)[]"
                else
                    "${convertKTypeToTSType(nestedType)}[]"
            }

            Map::class -> {
                val keyType = type.arguments.firstOrNull()?.type ?: return "{ [key: string]: any }"
                val valueType = type.arguments.getOrNull(1)?.type ?: return "{ [key: string]: any }"
                "{ [key: ${convertKTypeToTSType(keyType)}]: ${
                    convertKTypeToTSType(
                        valueType
                    )
                } }"
            }

            else -> {
                val nestedClass = (type.classifier as? KClass<*>) ?: return "any"
                val nestedClassName = nestedClass.simpleName ?: return "any"
                // Will create an interface for another type
                convertKClassInternal(nestedClass).replace(nestedClass.qualifiedName!!, nestedClassName)
                nestedClassName
            }
        }
    }
}

fun isUserType(type: KClass<*>): Boolean {
    return when (type) {
        String::class -> false
        // ! Conversion not tested
        Timestamp::class -> false
        Date::class -> false
        Char::class -> false
        Int::class -> false
        Long::class -> false
        Float::class -> false
        Double::class -> false
        Byte::class -> false
        Boolean::class -> false
        List::class -> false
        Map::class -> false
        else -> true
    }
}
