package com.example.financasapp.data.model

data class Expense(
    val id: Long,
    val amount: Double,
    val description: String,
    val month: Int,
    val year: Int
)

