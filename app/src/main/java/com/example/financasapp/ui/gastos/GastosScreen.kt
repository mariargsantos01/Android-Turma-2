package com.example.financasapp.ui.gastos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.financasapp.ui.components.formatChartLabel
import com.example.financasapp.ui.components.formatCurrency
import com.example.financasapp.ui.components.formatMonthYear
import com.example.financasapp.viewmodel.GastosEvent
import com.example.financasapp.viewmodel.GastosUiState
import java.util.Calendar

private val MONTH_NAMES = listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastosScreen(
    uiState: GastosUiState,
    onEvent: (GastosEvent) -> Unit
) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val listState = rememberLazyListState()
    var showMonthMenu by remember { mutableStateOf(false) }
    var showYearMenu by remember { mutableStateOf(false) }
    val yearOptions = remember(currentYear) { (2000..(currentYear + 20)).toList().sortedDescending() }
    var chartYear by remember(currentYear) { mutableStateOf(currentYear) }
    val chartYearOptions = remember(uiState.gastos, currentYear) {
        val dataYears = uiState.gastos.map { it.year }
        val minYear = minOf(2000, dataYears.minOrNull() ?: 2000)
        val maxYear = maxOf(currentYear + 20, dataYears.maxOrNull() ?: currentYear)
        (minYear..maxYear).toList().sortedDescending()
    }
    val monthlyTotals = remember(uiState.gastos, chartYear) {
        (1..12).map { month ->
            uiState.gastos.filter { it.month == month && it.year == chartYear }.sumOf { it.amount }
        }
    }

    // Sempre que entrar em modo de edicao, rola para o topo (item 0 = cabecalho/formulario)
    LaunchedEffect(uiState.editingId) {
        if (uiState.editingId != null) {
            listState.animateScrollToItem(0)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        // ── Cabecalho ───────────────────────────────────────────────────────
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp), modifier = Modifier.padding(14.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Text("Gastos e Despesas", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "Registre todos os seus gastos e despesas mensais",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
        }

        // ── Totalizador ──────────────────────────────────────────────────────
        item {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = "Total do mes atual: ${formatCurrency(uiState.totalMesAtual)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // ── Formulario ──────────────────────────────────────────────────────
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val fieldShape = RoundedCornerShape(14.dp)
                    Text(
                        text = if (uiState.editingId == null) "Novo gasto" else "Editar gasto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    OutlinedTextField(
                        value = uiState.valorInput,
                        onValueChange = { onEvent(GastosEvent.OnValorChange(it)) },
                        label = { Text("Valor") },
                        placeholder = { Text("Ex: 1.250,90") },
                        shape = fieldShape,
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Filled.AttachMoney, contentDescription = null) },
                        // Teclado de texto garante entrada de virgula em diferentes teclados Android.
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = uiState.descricaoInput,
                        onValueChange = { onEvent(GastosEvent.OnDescricaoChange(it)) },
                        label = { Text("Descricao") },
                        placeholder = { Text("Ex: Aluguel, mercado, internet") },
                        shape = fieldShape,
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Filled.Description, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = uiState.mesInput,
                            onValueChange = {},
                            label = { Text("Mes") },
                            placeholder = { Text("MM") },
                            shape = fieldShape,
                            singleLine = true,
                            readOnly = true,
                            leadingIcon = {
                                Box {
                                    IconButton(onClick = { showMonthMenu = true }) {
                                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Selecionar mes")
                                    }
                                    DropdownMenu(expanded = showMonthMenu, onDismissRequest = { showMonthMenu = false }) {
                                        MONTH_NAMES.forEachIndexed { index, name ->
                                            DropdownMenuItem(
                                                text = { Text(name) },
                                                onClick = {
                                                    onEvent(GastosEvent.OnMesChange((index + 1).toString()))
                                                    showMonthMenu = false
                                                }
                                            )
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = uiState.anoInput,
                            onValueChange = {},
                            label = { Text("Ano") },
                            placeholder = { Text("AAAA") },
                            shape = fieldShape,
                            singleLine = true,
                            readOnly = true,
                            leadingIcon = {
                                Box {
                                    IconButton(onClick = { showYearMenu = true }) {
                                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Selecionar ano")
                                    }
                                    DropdownMenu(expanded = showYearMenu, onDismissRequest = { showYearMenu = false }) {
                                        yearOptions.forEach { year ->
                                            DropdownMenuItem(
                                                text = { Text(year.toString()) },
                                                onClick = {
                                                    onEvent(GastosEvent.OnAnoChange(year.toString()))
                                                    showYearMenu = false
                                                }
                                            )
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { onEvent(GastosEvent.Save) }, modifier = Modifier.weight(1f)) {
                            Text(if (uiState.editingId == null) "Adicionar" else "Salvar")
                        }
                        if (uiState.editingId != null) {
                            Button(onClick = { onEvent(GastosEvent.CancelEdit) }, modifier = Modifier.weight(1f)) {
                                Text("Cancelar")
                            }
                        }
                    }
                }
            }
        }

        // ── Erro ────────────────────────────────────────────────────────────
        item {
            AnimatedVisibility(
                visible = uiState.errorMessage != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(text = uiState.errorMessage.orEmpty(), color = Color.Red)
            }
        }

        item {
            AnimatedVisibility(
                visible = uiState.successMessage != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(text = uiState.successMessage.orEmpty(), color = MaterialTheme.colorScheme.primary)
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Totais por mes",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        YearDropdown(
                            selectedYear = chartYear,
                            availableYears = chartYearOptions,
                            onYearSelected = { chartYear = it }
                        )
                    }
                    AnnualExpensesChart(monthlyTotals = monthlyTotals)
                }
            }
        }

        // ── Lancamentos ──────────────────────────────────────────────────────
        item {
            Text(
                "Lancamentos (${uiState.gastos.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (uiState.gastos.isEmpty()) {
            item {
                Text("Nenhum gasto registrado ainda.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(uiState.gastos, key = { it.id }) { gasto ->
                ElevatedCard(modifier = Modifier.fillMaxWidth().animateContentSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = gasto.description.ifBlank { "Sem descricao" },
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = formatCurrency(gasto.amount),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = formatMonthYear(gasto.month, gasto.year),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Row {
                            IconButton(onClick = { onEvent(GastosEvent.Edit(gasto.id)) }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = { onEvent(GastosEvent.Delete(gasto.id)) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Remover", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun YearDropdown(
    selectedYear: Int,
    availableYears: List<Int>,
    onYearSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        OutlinedTextField(
            value = selectedYear.toString(),
            onValueChange = {},
            readOnly = true,
            label = { Text("Ano") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .width(120.dp)
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            availableYears.forEach { year ->
                DropdownMenuItem(
                    text = { Text(year.toString()) },
                    onClick = {
                        onYearSelected(year)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun AnnualExpensesChart(monthlyTotals: List<Double>) {
    val maxValue = monthlyTotals.maxOrNull()?.takeIf { it > 0.0 } ?: 1.0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        monthlyTotals.forEachIndexed { index, total ->
            val ratio = (total / maxValue).toFloat().coerceIn(0f, 1f)
            val animatedRatio by animateFloatAsState(
                targetValue = ratio,
                animationSpec = tween(durationMillis = 700),
                label = "expense-bar-$index"
            )
            val barHeight = (16f + (100f * animatedRatio)).dp

            Column(
                modifier = Modifier.width(72.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                if (total > 0.0) {
                    Text(
                        text = formatChartLabel(total),
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 16.dp)
                        .height(barHeight)
                        .background(
                            color = if (total > 0.0) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.small
                        )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = MONTH_NAMES[index],
                    style = MaterialTheme.typography.labelSmall,
                    color = if (total > 0.0) MaterialTheme.colorScheme.onSurface else Color.Gray,
                    fontWeight = if (total > 0.0) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

