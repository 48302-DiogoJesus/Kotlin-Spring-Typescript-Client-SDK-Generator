package com.example.demo

import com.example.demo.lib.utils.TSTypesGenerator
import com.example.demo.testSubjects.*
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
