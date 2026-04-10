package com.example.financasapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.financasapp.ui.theme.ExpenseRed
import com.example.financasapp.ui.theme.IncomeGreen

@Composable
fun BalanceCard(
    title: String,
    value: Double,
    modifier: Modifier = Modifier
) {
    val balanceColor = if (value >= 0.0) IncomeGreen else ExpenseRed
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    imageVector = Icons.Filled.AccountBalanceWallet,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(text = title, style = MaterialTheme.typography.titleMedium)
            }
            Text(
                text = formatCurrency(value),
                style = MaterialTheme.typography.headlineSmall,
                color = balanceColor
            )
            Text(
                text = if (value >= 0.0) "Saude financeira positiva" else "Atencao ao fluxo de caixa",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}

