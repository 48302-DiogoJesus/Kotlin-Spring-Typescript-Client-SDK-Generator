package clientSdkGenerator

import clientSdkGenerator.utils.TSTypesGenerator
import clientSdkGenerator.testSubjects.QUERY_TYPE
import clientSdkGenerator.testSubjects.QUERY_TYPE_W_NULLABLES_CONVERTED
import clientSdkGenerator.testSubjects.queryType
import clientSdkGenerator.testSubjects.queryTypeWithNullables
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TSTypesGeneratorTestsMap {
    @Test
    fun withoutCustomNestedTypes() {
        val tsTypeGenerator = TSTypesGenerator()
        val (result) = tsTypeGenerator.fromMap(queryType)

        assertEquals(QUERY_TYPE, result)
    }

    @Test
    fun queryTypeWithNullables() {
        val tsTypeGenerator = TSTypesGenerator()
        val (result) = tsTypeGenerator.fromMap(queryTypeWithNullables)

        assertEquals(QUERY_TYPE_W_NULLABLES_CONVERTED, result)
    }
}
