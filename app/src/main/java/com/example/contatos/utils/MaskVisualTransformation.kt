package com.example.contatos.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

interface DigitMaskFormatter {
    val maxLength: Int
    fun digitsOnly(value: String): String
    fun format(value: String): String
}

class MaskVisualTransformation(
    private val formatter: DigitMaskFormatter
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val raw = formatter.digitsOnly(text.text).take(formatter.maxLength)
        val formatted = formatter.format(raw)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val safeOffset = offset.coerceIn(0, raw.length)
                if (safeOffset == 0) return 0
                return formatter.format(raw.take(safeOffset)).length
            }

            override fun transformedToOriginal(offset: Int): Int {
                val safeOffset = offset.coerceIn(0, formatted.length)
                return formatted
                    .take(safeOffset)
                    .count(Char::isDigit)
                    .coerceIn(0, raw.length)
            }
        }

        return TransformedText(
            text = AnnotatedString(formatted),
            offsetMapping = offsetMapping
        )
    }
}

