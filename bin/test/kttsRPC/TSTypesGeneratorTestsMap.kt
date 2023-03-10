package kttsRPC

import kttsRPC.utils.TSTypesGenerator
import kttsRPC.testSubjects.QUERY_TYPE
import kttsRPC.testSubjects.QUERY_TYPE_W_NULLABLES_CONVERTED
import kttsRPC.testSubjects.queryType
import kttsRPC.testSubjects.queryTypeWithNullables
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
