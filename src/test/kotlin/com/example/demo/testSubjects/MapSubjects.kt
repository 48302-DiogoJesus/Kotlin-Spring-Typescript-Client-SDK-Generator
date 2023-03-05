package com.example.demo.testSubjects

import kotlin.reflect.full.createType

val queryType = mapOf(
    "search" to String::class.createType(),
    "orderAsc" to Boolean::class.createType(),
    "limit" to Int::class.createType()
)

const val QUERY_TYPE = "{\tsearch: string,\n" +
    "\torderAsc: boolean,\n" +
    "\tlimit: number}"

val queryTypeWithNullables = mapOf(
    "search" to String::class.createType(),
    "orderAsc" to Boolean::class.createType(nullable = true),
    "limit" to Int::class.createType(nullable = true)
)

const val QUERY_TYPE_W_NULLABLES_CONVERTED = "{\tsearch: string,\n" +
    "\torderAsc?: boolean,\n" +
    "\tlimit?: number}"
