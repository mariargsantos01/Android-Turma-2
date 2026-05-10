package com.example.contatos.utils

object PhoneMask : DigitMaskFormatter {

    override val maxLength: Int = 11

    override fun digitsOnly(value: String): String = value.filter(Char::isDigit).take(maxLength)

    override fun format(value: String): String {
        val digits = digitsOnly(value)
        if (digits.isEmpty()) return ""

        val ddd = digits.take(2)
        val firstPart = digits.drop(2).take(5)
        val secondPart = digits.drop(7).take(4)

        return buildString {
            append("(")
            append(ddd)
            if (digits.length >= 2) {
                append(")")
            }
            if (digits.length > 2) {
                append(" ")
                append(firstPart)
            }
            if (digits.length > 7) {
                append("-")
                append(secondPart)
            }
        }
    }

    fun isValid(value: String): Boolean = digitsOnly(value).length == maxLength
}

