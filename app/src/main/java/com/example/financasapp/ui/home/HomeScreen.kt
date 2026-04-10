package com.example.financasapp.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.financasapp.ui.components.BalanceCard
import com.example.financasapp.ui.components.formatCurrency
import com.example.financasapp.ui.theme.Blue40
import com.example.financasapp.ui.theme.ExpenseRed
import com.example.financasapp.ui.theme.IncomeGreen
import com.example.financasapp.viewmodel.HomeEvent
import com.example.financasapp.viewmodel.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    onNavigateGanhos: () -> Unit,
    onNavigateGastos: () -> Unit,
    onNavigateSonhos: () -> Unit
) {
    var showContent by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { showContent = true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.Savings, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text("Financas App", style = MaterialTheme.typography.headlineSmall)
                }
                Text(
                    text = "Controle de ganhos, gastos e metas em um so lugar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }

        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 })
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                BalanceCard(title = "Saldo atual", value = uiState.saldoAtual)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Ganhos",
                        value = formatCurrency(uiState.totalGanhos),
                        icon = { Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null) }
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Gastos",
                        value = formatCurrency(uiState.totalGastos),
                        icon = { Icon(Icons.AutoMirrored.Filled.TrendingDown, contentDescription = null) }
                    )
                }

                StatCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Sonhos ativos",
                    value = uiState.quantidadeSonhos.toString(),
                    icon = { Icon(Icons.Filled.Savings, contentDescription = null) }
                )

                GainsVsExpensesChart(
                    ganhos = uiState.totalGanhos,
                    gastos = uiState.totalGastos
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ElevatedButton(
                        onClick = onNavigateGanhos,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = IncomeGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null)
                        Text(
                            text = "Ganhos",
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    ElevatedButton(
                        onClick = onNavigateGastos,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = ExpenseRed,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.AutoMirrored.Filled.TrendingDown, contentDescription = null)
                        Text(
                            text = "Gastos",
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    ElevatedButton(
                        onClick = onNavigateSonhos,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = Blue40,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(Icons.Filled.Savings, contentDescription = null)
                        Text(
                            text = "Sonhos",
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                ElevatedButton(
                    onClick = { onEvent(HomeEvent.Refresh) },
                    colors = ButtonDefaults.elevatedButtonColors(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = null)
                    Text(" Atualizar dados")
                }
            }
        }
    }
}

@Composable
private fun GainsVsExpensesChart(
    ganhos: Double,
    gastos: Double,
    modifier: Modifier = Modifier
) {
    val total = (ganhos + gastos).coerceAtLeast(0.0)
    val ganhosWeight = if (total == 0.0) 0.5f else (ganhos / total).toFloat().coerceIn(0f, 1f).coerceAtLeast(0.01f)
    val gastosWeight = if (total == 0.0) 0.5f else (gastos / total).toFloat().coerceIn(0f, 1f).coerceAtLeast(0.01f)
    val media = (ganhos + gastos) / 2.0

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Comparativo Ganhos x Gastos", style = MaterialTheme.typography.titleMedium)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Ganhos: ${formatCurrency(ganhos)}", color = IncomeGreen, style = MaterialTheme.typography.bodyMedium)
                    Text("Gastos: ${formatCurrency(gastos)}", color = ExpenseRed, style = MaterialTheme.typography.bodyMedium)
                    Text("Media: ${formatCurrency(media)}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }

                // Coluna unica empilhada: verde (ganhos) + vermelho (gastos)
                Box(
                    modifier = Modifier
                        .height(140.dp)
                        .width(52.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(10.dp)),
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(gastosWeight)
                                .background(ExpenseRed)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(ganhosWeight)
                                .background(IncomeGreen)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier,
    title: String,
    value: String,
    icon: @Composable () -> Unit
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                icon()
                Text(text = title, style = MaterialTheme.typography.titleSmall)
            }
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

