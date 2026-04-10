package com.example.financasapp.ui.components

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

private val ptBrLocale: Locale = Locale.Builder()
    .setLanguage("pt")
    .setRegion("BR")
    .build()

fun formatCurrency(value: Double): String = NumberFormat.getCurrencyInstance(ptBrLocale).format(value)

fun formatMonthYear(month: Int, year: Int): String = "%02d/%d".format(month, year)

/**
 * Formata valor para preencher campo de input monetario no padrao brasileiro.
 * Ex: 5842.47 -> 5.842,47
 */
fun formatMoneyInput(value: Double): String {
    val symbols = DecimalFormatSymbols(ptBrLocale)
    return DecimalFormat("#,##0.00", symbols).format(value)
}

/**
 * Formata valores para labels do gráfico sem arredondamento:
 * - Remove símbolo "R$" para economizar espaço
 * - Mostra centavos apenas quando diferentes de zero
 * Ex: 3600.0   → "3.600"
 *     3600.5   → "3.600,50"
 *     450.0    → "450"
 *     1500000.0→ "1.500.000"
 */
fun formatChartLabel(value: Double): String {
    if (value == 0.0) return ""
    val symbols = DecimalFormatSymbols(ptBrLocale)
    val hasCents = (value % 1.0) != 0.0
    val pattern = if (hasCents) "#,##0.00" else "#,##0"
    return DecimalFormat(pattern, symbols).format(value)
}

/**
 * Converte input monetário digitado pelo usuário para Double.
 * Suporta formatos:
 *   - Brasileiro: 3.000,21  → 3000.21
 *   - Só vírgula: 3000,21   → 3000.21
 *   - Só ponto:   3000.21   → 3000.21 (ou 3.000 → 3000)
 *   - Inteiro:    3000      → 3000.0
 * Retorna null se não for possível interpretar.
 */
fun parseMoneyInput(input: String): Double? {
    val trimmed = input.trim()
    if (trimmed.isBlank()) return null

    return when {
        // Formato brasileiro: ponto como milhar, vírgula como decimal  → "3.000,21"
        trimmed.contains('.') && trimmed.contains(',') -> {
            trimmed.replace(".", "").replace(',', '.').toDoubleOrNull()
        }
        // Só vírgula como separador decimal → "3000,21"
        trimmed.contains(',') -> {
            trimmed.replace(',', '.').toDoubleOrNull()
        }
        // Só ponto — pode ser milhar ("3.000") ou decimal ("3.21")
        // Regra: se houver exatamente 3 dígitos após o ponto → milhar; senão decimal
        trimmed.contains('.') -> {
            val parts = trimmed.split('.')
            if (parts.size == 2 && parts[1].length == 3 && parts[1].all { it.isDigit() } && parts[0].isNotEmpty()) {
                // "3.000" → 3000
                (parts[0] + parts[1]).toDoubleOrNull()
            } else {
                trimmed.toDoubleOrNull()
            }
        }
        // Número sem separador
        else -> trimmed.toDoubleOrNull()
    }
}


