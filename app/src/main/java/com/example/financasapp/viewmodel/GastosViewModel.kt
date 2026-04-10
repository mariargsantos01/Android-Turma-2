package com.example.financasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financasapp.data.model.Expense
import com.example.financasapp.data.repository.FinanceRepository
import com.example.financasapp.ui.components.formatMoneyInput
import com.example.financasapp.ui.components.parseMoneyInput
import java.util.Calendar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GastosUiState(
    val gastos: List<Expense> = emptyList(),
    val valorInput: String = "",
    val descricaoInput: String = "",
    val mesInput: String = currentMonth().toString(),
    val anoInput: String = currentYear().toString(),
    val totalMesAtual: Double = 0.0,
    val editingId: Long? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

sealed class GastosEvent {
    data class OnValorChange(val value: String) : GastosEvent()
    data class OnDescricaoChange(val value: String) : GastosEvent()
    data class OnMesChange(val value: String) : GastosEvent()
    data class OnAnoChange(val value: String) : GastosEvent()
    data object Save : GastosEvent()
    data class Edit(val id: Long) : GastosEvent()
    data class Delete(val id: Long) : GastosEvent()
    data object CancelEdit : GastosEvent()
    data object DismissError : GastosEvent()
    data object DismissSuccess : GastosEvent()
}

class GastosViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GastosUiState())
    val uiState: StateFlow<GastosUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeExpenses().collect { gastos ->
                _uiState.update { current ->
                    current.copy(
                        gastos = gastos,
                        totalMesAtual = gastos.sumOf {
                            if (it.month == currentMonth() && it.year == currentYear()) it.amount else 0.0
                        }
                    )
                }
            }
        }
    }

    fun onEvent(event: GastosEvent) {
        when (event) {
            is GastosEvent.OnValorChange -> _uiState.update { it.copy(valorInput = event.value, successMessage = null) }
            is GastosEvent.OnDescricaoChange -> _uiState.update { it.copy(descricaoInput = event.value, successMessage = null) }
            is GastosEvent.OnMesChange -> _uiState.update { it.copy(mesInput = event.value, successMessage = null) }
            is GastosEvent.OnAnoChange -> _uiState.update { it.copy(anoInput = event.value, successMessage = null) }
            is GastosEvent.Delete -> delete(event.id)
            is GastosEvent.Edit -> edit(event.id)
            GastosEvent.Save -> save()
            GastosEvent.CancelEdit -> resetForm()
            GastosEvent.DismissError -> _uiState.update { it.copy(errorMessage = null) }
            GastosEvent.DismissSuccess -> _uiState.update { it.copy(successMessage = null) }
        }
    }

    private fun save() {
        val state = _uiState.value
        val isEditing = state.editingId != null
        val valor = parseMoneyInput(state.valorInput)
        val mes = state.mesInput.toIntOrNull()
        val ano = state.anoInput.toIntOrNull()

        if (valor == null || valor <= 0 || mes == null || mes !in 1..12 || ano == null || ano < 2000) {
            _uiState.update { it.copy(errorMessage = "Preencha valor, mes e ano com dados validos.") }
            return
        }

        viewModelScope.launch {
            repository.upsertExpense(
                Expense(
                    id = state.editingId ?: 0,
                    amount = valor,
                    description = state.descricaoInput.trim(),
                    month = mes,
                    year = ano
                )
            )
            resetForm(successMessage = if (isEditing) "Gasto atualizado com sucesso." else "Gasto adicionado com sucesso.")
        }
    }

    private fun edit(id: Long) {
        val expense = _uiState.value.gastos.firstOrNull { it.id == id } ?: return
        _uiState.update {
            it.copy(
                valorInput = formatMoneyInput(expense.amount),
                descricaoInput = expense.description,
                mesInput = expense.month.toString(),
                anoInput = expense.year.toString(),
                editingId = expense.id,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    private fun delete(id: Long) {
        viewModelScope.launch {
            repository.deleteExpense(id)
            _uiState.update { it.copy(successMessage = "Gasto removido com sucesso.") }
        }
    }

    private fun resetForm(successMessage: String? = _uiState.value.successMessage) {
        _uiState.update {
            it.copy(
                valorInput = "",
                descricaoInput = "",
                mesInput = currentMonth().toString(),
                anoInput = currentYear().toString(),
                editingId = null,
                errorMessage = null,
                successMessage = successMessage
            )
        }
    }
}

private fun currentMonth(): Int = Calendar.getInstance().get(Calendar.MONTH) + 1
private fun currentYear(): Int = Calendar.getInstance().get(Calendar.YEAR)

