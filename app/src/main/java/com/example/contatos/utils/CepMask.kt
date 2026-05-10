package com.example.contatos.utils

object CepMask : DigitMaskFormatter {

    override val maxLength: Int = 8

    override fun digitsOnly(value: String): String = value.filter(Char::isDigit).take(maxLength)

    override fun format(value: String): String {
        val digits = digitsOnly(value)
        if (digits.isEmpty()) return ""

        val prefix = digits.take(5)
        val suffix = digits.drop(5).take(3)

        return buildString {
            append(prefix)
            if (digits.length > 5) {
                append("-")
                append(suffix)
            }
        }
    }

    fun isValid(value: String): Boolean = digitsOnly(value).length == maxLength
}

