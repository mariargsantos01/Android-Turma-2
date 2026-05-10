package com.example.contatos.utils

import android.util.Patterns

object EmailValidator {

    fun isValid(value: String): Boolean {
        val email = value.trim()
        if (email.isBlank()) return false
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) return false

        val domain = email.substringAfter('@', missingDelimiterValue = "")
        if (domain.isBlank() || !domain.contains('.')) return false

        val parts = domain.split('.')
        if (parts.any { it.isBlank() }) return false
        if (parts.last().length < 2) return false

        return true
    }
}

