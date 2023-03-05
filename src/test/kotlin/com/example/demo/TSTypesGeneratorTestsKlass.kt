package com.example.demo

import com.example.demo.lib.utils.TSTypesGenerator
import com.example.demo.testSubjects.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TSTypesGeneratorTestsKlass {
    @Test
    fun withoutCustomNestedTypes() {
        val tsTypeGenerator = TSTypesGenerator()
        val (result, typesCreated) = tsTypeGenerator.fromKClass(PersonWithoutFriends::class)

        assertEquals(1, typesCreated.size)
        assertEquals(PERSON_WITHOUT_FRIENDS_CONVERTED, result)
    }

    @Test
    fun withCustomNestedTypes() {
        val tsTypeGenerator = TSTypesGenerator()
        val (result, typesCreated) = tsTypeGenerator.fromKClass(Person::class)


        assertEquals(2, typesCreated.size)
        assertEquals(PERSON_CONVERTED, result)

        val secondaryType = typesCreated.values.toList()[0]
        assertEquals(PERSON_WITHOUT_FRIENDS_CONVERTED, secondaryType)
    }

    @Test
    fun with3LVLCustomNestedTypes() {
        val tsTypeGenerator = TSTypesGenerator()
        val (result, typesCreated) = tsTypeGenerator.fromKClass(School::class)

        assertEquals(3, typesCreated.size)

        val typesCreatedValues = typesCreated.values.toList()

        assertEquals(SCHOOL_CONVERTED, result)
        assertEquals(typesCreatedValues[0], PERSON_WITHOUT_FRIENDS_CONVERTED)
        assertEquals(typesCreatedValues[1], PERSON_CONVERTED)
    }
}
