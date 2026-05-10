package com.example.contatos.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateMask : DigitMaskFormatter {

    private const val PATTERN = "dd/MM/yyyy"
    private val utcTimeZone: TimeZone = TimeZone.getTimeZone("UTC")

    override val maxLength: Int = 8

    override fun digitsOnly(value: String): String = value.filter(Char::isDigit).take(maxLength)

    override fun format(value: String): String {
        val digits = digitsOnly(value)
        if (digits.isEmpty()) return ""

        val day = digits.take(2)
        val month = digits.drop(2).take(2)
        val year = digits.drop(4).take(4)

        return buildString {
            append(day)
            if (digits.length > 2) {
                append("/")
                append(month)
            }
            if (digits.length > 4) {
                append("/")
                append(year)
            }
        }
    }

    fun isValid(value: String): Boolean {
        val digits = digitsOnly(value)
        if (digits.length != maxLength) return false

        return try {
            formatter().parse(format(digits))
            true
        } catch (_: Exception) {
            false
        }
    }

    fun toEpochMillis(value: String): Long? {
        if (!isValid(value)) return null
        return try {
            formatter(timeZone = utcTimeZone).parse(value)?.time
        } catch (_: Exception) {
            null
        }
    }

    fun fromEpochMillis(value: Long): String {
        return formatter(timeZone = utcTimeZone).format(Date(value))
    }

    private fun formatter(timeZone: TimeZone? = null): SimpleDateFormat {
        return SimpleDateFormat(PATTERN, Locale.getDefault()).apply {
            isLenient = false
            timeZone?.let { this.timeZone = it }
        }
    }
}

