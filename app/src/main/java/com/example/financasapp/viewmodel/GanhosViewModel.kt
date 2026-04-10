package com.example.financasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financasapp.data.model.Income
import com.example.financasapp.data.repository.FinanceRepository
import com.example.financasapp.ui.components.formatMoneyInput
import com.example.financasapp.ui.components.parseMoneyInput
import java.util.Calendar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GanhosUiState(
    val ganhos: List<Income> = emptyList(),
    val valorInput: String = "",
    val descricaoInput: String = "",
    val mesInput: String = currentMonth().toString(),
    val anoInput: String = currentYear().toString(),
    val totalMesAtual: Double = 0.0,
    val editingId: Long? = null,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

sealed class GanhosEvent {
    data class OnValorChange(val value: String) : GanhosEvent()
    data class OnDescricaoChange(val value: String) : GanhosEvent()
    data class OnMesChange(val value: String) : GanhosEvent()
    data class OnAnoChange(val value: String) : GanhosEvent()
    data object Save : GanhosEvent()
    data class Edit(val id: Long) : GanhosEvent()
    data class Delete(val id: Long) : GanhosEvent()
    data object CancelEdit : GanhosEvent()
    data object DismissError : GanhosEvent()
    data object DismissSuccess : GanhosEvent()
}

class GanhosViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GanhosUiState())
    val uiState: StateFlow<GanhosUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeIncomes().collect { ganhos ->
                _uiState.update { current ->
                    current.copy(
                        ganhos = ganhos,
                        totalMesAtual = ganhos.sumOf {
                            if (it.month == currentMonth() && it.year == currentYear()) it.amount else 0.0
                        }
                    )
                }
            }
        }
    }

    fun onEvent(event: GanhosEvent) {
        when (event) {
            is GanhosEvent.OnValorChange -> _uiState.update { it.copy(valorInput = event.value, successMessage = null) }
            is GanhosEvent.OnDescricaoChange -> _uiState.update { it.copy(descricaoInput = event.value, successMessage = null) }
            is GanhosEvent.OnMesChange -> _uiState.update { it.copy(mesInput = event.value, successMessage = null) }
            is GanhosEvent.OnAnoChange -> _uiState.update { it.copy(anoInput = event.value, successMessage = null) }
            is GanhosEvent.Delete -> delete(event.id)
            is GanhosEvent.Edit -> edit(event.id)
            GanhosEvent.Save -> save()
            GanhosEvent.CancelEdit -> resetForm()
            GanhosEvent.DismissError -> _uiState.update { it.copy(errorMessage = null) }
            GanhosEvent.DismissSuccess -> _uiState.update { it.copy(successMessage = null) }
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
            repository.upsertIncome(
                Income(
                    id = state.editingId ?: 0,
                    amount = valor,
                    description = state.descricaoInput.trim(),
                    month = mes,
                    year = ano
                )
            )
            resetForm(successMessage = if (isEditing) "Ganho atualizado com sucesso." else "Ganho adicionado com sucesso.")
        }
    }

    private fun edit(id: Long) {
        val income = _uiState.value.ganhos.firstOrNull { it.id == id } ?: return
        _uiState.update {
            it.copy(
                valorInput = formatMoneyInput(income.amount),
                descricaoInput = income.description,
                mesInput = income.month.toString(),
                anoInput = income.year.toString(),
                editingId = income.id,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    private fun delete(id: Long) {
        viewModelScope.launch {
            repository.deleteIncome(id)
            _uiState.update { it.copy(successMessage = "Ganho removido com sucesso.") }
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

