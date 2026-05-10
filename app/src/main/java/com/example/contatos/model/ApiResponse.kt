package com.example.contatos.model

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null
)

