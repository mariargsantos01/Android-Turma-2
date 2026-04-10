package com.example.financasapp.ui.sonhos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.financasapp.ui.components.formatCurrency
import com.example.financasapp.viewmodel.DreamStatus
import com.example.financasapp.viewmodel.SonhosEvent
import com.example.financasapp.viewmodel.SonhosUiState
import java.util.Calendar

@Composable
fun SonhosScreen(
    uiState: SonhosUiState,
    onEvent: (SonhosEvent) -> Unit
) {
    val calendar = Calendar.getInstance()
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH) + 1) }
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var showMonthMenu by remember { mutableStateOf(false) }
    var showYearMenu by remember { mutableStateOf(false) }
    val yearOptions = remember(selectedYear) { (2000..2100).toList().sortedDescending() }
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // ── Cabecalho ───────────────────────────────────────────────────────
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Filled.Savings, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text(text = "Sonhos e Desejos", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "Acompanhe seus objetivos financeiros com estimativa de tempo",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }

        // ── Formulario ──────────────────────────────────────────────────────
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val fieldShape = RoundedCornerShape(14.dp)
                    Text(
                        text = "Novo sonho",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    OutlinedTextField(
                        value = uiState.nomeInput,
                        onValueChange = { onEvent(SonhosEvent.OnNomeChange(it)) },
                        label = { Text("Nome do sonho") },
                        placeholder = { Text("Ex: Viagem, Casa, Carro") },
                        shape = fieldShape,
                        singleLine = true,
                        leadingIcon = { Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.valorObjetivoInput,
                        onValueChange = { onEvent(SonhosEvent.OnValorObjetivoChange(it)) },
                        label = { Text("Valor objetivo") },
                        placeholder = { Text("Ex: 50.000,00") },
                        shape = fieldShape,
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Filled.AttachMoney, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = "%02d".format(selectedMonth),
                            onValueChange = {},
                            label = { Text("Mes") },
                            shape = fieldShape,
                            singleLine = true,
                            readOnly = true,
                            leadingIcon = {
                                IconButton(onClick = { showMonthMenu = true }) {
                                    Icon(Icons.Filled.CalendarMonth, contentDescription = "Selecionar mes")
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                        DropdownMenu(expanded = showMonthMenu, onDismissRequest = { showMonthMenu = false }) {
                            MONTH_NAMES.forEachIndexed { index, name ->
                                DropdownMenuItem(
                                    text = { Text(name) },
                                    onClick = {
                                        selectedMonth = index + 1
                                        showMonthMenu = false
                                    }
                                )
                            }
                        }
                        OutlinedTextField(
                            value = selectedYear.toString(),
                            onValueChange = {},
                            label = { Text("Ano") },
                            shape = fieldShape,
                            singleLine = true,
                            readOnly = true,
                            leadingIcon = {
                                IconButton(onClick = { showYearMenu = true }) {
                                    Icon(Icons.Filled.CalendarMonth, contentDescription = "Selecionar ano")
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                        DropdownMenu(expanded = showYearMenu, onDismissRequest = { showYearMenu = false }) {
                            yearOptions.forEach { year ->
                                DropdownMenuItem(
                                    text = { Text(year.toString()) },
                                    onClick = {
                                        selectedYear = year
                                        showYearMenu = false
                                    }
                                )
                            }
                        }
                    }

                    Button(onClick = { onEvent(SonhosEvent.Save) }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Filled.Savings, contentDescription = null)
                        Text("Adicionar sonho")
                    }
                }
            }
        }

        // ── Mensagens ────────────────────────────────────────────────────────
        item {
            AnimatedVisibility(visible = uiState.errorMessage != null) {
                Text(text = uiState.errorMessage.orEmpty(), color = Color.Red)
            }
        }

        item {
            AnimatedVisibility(visible = uiState.successMessage != null) {
                Text(text = uiState.successMessage.orEmpty(), color = MaterialTheme.colorScheme.primary)
            }
        }

        // ── Lancamentos ──────────────────────────────────────────────────────
        item {
            Text(
                "Sonhos Cadastrados (${uiState.sonhos.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (uiState.sonhos.isEmpty()) {
            item {
                Text("Nenhum sonho registrado ainda.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(uiState.sonhos, key = { it.id }) { sonho ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = sonho.nome, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                            IconButton(onClick = { onEvent(SonhosEvent.Delete(sonho.id)) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Remover sonho", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                        Text(text = "Objetivo: ${formatCurrency(sonho.valorObjetivo)}", fontWeight = FontWeight.Medium)
                        Text(
                            text = "Saldo necessario: ${formatCurrency((sonho.valorObjetivo - (uiState.saldoAtual)).coerceAtLeast(0.0))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        val monthsRemaining = calculateMonthsRemaining(sonho.valorObjetivo, uiState.saldoAtual)
                        if (monthsRemaining > 0) {
                            Text(
                                text = "Tempo estimado: $monthsRemaining mes(es)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        StatusChip(status = sonho.status)
                        val animatedProgress = animateFloatAsState(
                            targetValue = sonho.progresso,
                            animationSpec = tween(durationMillis = 600),
                            label = "dream-progress"
                        )
                        LinearProgressIndicator(
                            progress = { animatedProgress.value },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        item { androidx.compose.foundation.layout.Spacer(modifier = Modifier.padding(8.dp)) }
    }
}

private val MONTH_NAMES = listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez")

@Composable
private fun StatusChip(status: DreamStatus) {
    val (label, icon) = when (status) {
        DreamStatus.ATINGIVEL -> "Atingivel" to Icons.Filled.CheckCircle
        DreamStatus.PARCIALMENTE_ATINGIVEL -> "Parcialmente atingivel" to Icons.AutoMirrored.Filled.TrendingUp
        DreamStatus.NAO_ATINGIVEL -> "Nao atingivel" to Icons.Filled.ErrorOutline
    }
    FilterChip(
        selected = true,
        onClick = {},
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null) }
    )
}

/**
 * Calcula quantos meses sao necessarios para atingir o objetivo do sonho.
 * Usa a media do saldo atual como taxa mensal de acumulo.
 */
private fun calculateMonthsRemaining(targetAmount: Double, currentBalance: Double): Int {
    if (currentBalance >= targetAmount) return 0
    val remaining = targetAmount - currentBalance
    val monthlyRate = currentBalance.coerceAtLeast(1.0)
    return (remaining / monthlyRate).toInt() + 1
}

