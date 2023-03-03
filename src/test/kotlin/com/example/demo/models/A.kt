package com.example.demo.models

import java.sql.Date
import java.sql.Timestamp

data class Person(
    val id: Int,
    val name: String,
    val hobbies: List<Hobby>,
    val lastName: String?
)

data class Hobby(
    val createdAt: Timestamp,
    val updatedAt: Date,
    val name: String
)
