package kttsRPC.rpclib

import kttsRPC.types.HandlerMetadata
import kttsRPC.types.TypeName
import kttsRPC.utils.TSTypesGenerator
import java.io.File

fun writeTypesFile(handlersMetadata: List<HandlerMetadata>, typesFilePath: String): Set<TypeName> {
    val writtenTypes: MutableMap<TypeName, String> = mutableMapOf()
    val addUnique = { (typeName, typeDefinition): Map.Entry<TypeName, String> ->
        if (!writtenTypes.containsKey(typeName)) {
            writtenTypes[typeName] = typeDefinition
        }
    }

    val tsTypeGenerator = TSTypesGenerator()
    handlersMetadata.forEach { handler ->
        if (handler.requestBodyType != null) {
            // Request body is needed for this handler
            val res = tsTypeGenerator.fromKClass(handler.requestBodyType, handler.requestBodyType.simpleName)
            res.typesCreated.forEach { addUnique(it) }
        }

        // Response is required, even if its Unit (ts void)
        // If it's Unit don't generate a type definition for it
        if (handler.successResponseType != Unit::class) {
            val res = tsTypeGenerator.fromKClass(handler.successResponseType, handler.successResponseType.simpleName)
            res.typesCreated.forEach { addUnique(it) }
        }

        if (handler.errorResponseType != null && handler.errorResponseType != Unit::class) {
            val res = tsTypeGenerator.fromKClass(handler.errorResponseType, handler.errorResponseType.simpleName)
            res.typesCreated.forEach { addUnique(it) }
        }
    }

    File(typesFilePath)
        .also {
            // So that TSC does not complain about missing export statements. This happens if there
            // are no user-defined types (all types used are primitive or List/Map<primitive>)
            // Could be removed if in this function we knew which types were written to the UserTypes file
            it.writeText("export const tsDontComplain: never;\n\n")
            it.appendText(writtenTypes.values.joinToString("\n"))
        }

    return writtenTypes.keys
}
