package com.example.financasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financasapp.data.model.Dream
import com.example.financasapp.data.repository.FinanceRepository
import com.example.financasapp.ui.components.parseMoneyInput
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class DreamStatus {
    ATINGIVEL,
    PARCIALMENTE_ATINGIVEL,
    NAO_ATINGIVEL
}

data class DreamProgressUi(
    val id: Long,
    val nome: String,
    val valorObjetivo: Double,
    val progresso: Float,
    val status: DreamStatus
)

data class SonhosUiState(
    val sonhos: List<DreamProgressUi> = emptyList(),
    val nomeInput: String = "",
    val valorObjetivoInput: String = "",
    val saldoAtual: Double = 0.0,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

sealed class SonhosEvent {
    data class OnNomeChange(val value: String) : SonhosEvent()
    data class OnValorObjetivoChange(val value: String) : SonhosEvent()
    data object Save : SonhosEvent()
    data class Delete(val id: Long) : SonhosEvent()
    data object DismissError : SonhosEvent()
    data object DismissSuccess : SonhosEvent()
}

class SonhosViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SonhosUiState())
    val uiState: StateFlow<SonhosUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.observeDreams(),
                repository.observeIncomes(),
                repository.observeExpenses()
            ) { dreams, incomes, expenses ->
                val saldo = incomes.sumOf { it.amount } - expenses.sumOf { it.amount }
                Pair(dreams, saldo)
            }.collect { (dreams, saldo) ->
                _uiState.update { current ->
                    current.copy(
                        sonhos = dreams.map { dream -> dream.toProgress(saldo) },
                        saldoAtual = saldo
                    )
                }
            }
        }
    }

    fun onEvent(event: SonhosEvent) {
        when (event) {
            is SonhosEvent.OnNomeChange -> _uiState.update { it.copy(nomeInput = event.value, successMessage = null) }
            is SonhosEvent.OnValorObjetivoChange -> _uiState.update { it.copy(valorObjetivoInput = event.value, successMessage = null) }
            is SonhosEvent.Delete -> delete(event.id)
            SonhosEvent.Save -> save()
            SonhosEvent.DismissError -> _uiState.update { it.copy(errorMessage = null) }
            SonhosEvent.DismissSuccess -> _uiState.update { it.copy(successMessage = null) }
        }
    }

    private fun save() {
        val state = _uiState.value
        val nome = state.nomeInput.trim()
        val valorObjetivo = parseMoneyInput(state.valorObjetivoInput)

        if (nome.isBlank() || valorObjetivo == null || valorObjetivo <= 0) {
            _uiState.update { it.copy(errorMessage = "Informe nome e valor objetivo valido.") }
            return
        }

        viewModelScope.launch {
            repository.upsertDream(
                Dream(
                    id = 0,
                    name = nome,
                    targetAmount = valorObjetivo
                )
            )
            _uiState.update {
                it.copy(
                    nomeInput = "",
                    valorObjetivoInput = "",
                    errorMessage = null,
                    successMessage = "Sonho adicionado com sucesso."
                )
            }
        }
    }

    private fun delete(id: Long) {
        viewModelScope.launch {
            repository.deleteDream(id)
            _uiState.update { it.copy(successMessage = "Sonho removido com sucesso.") }
        }
    }
}

private fun Dream.toProgress(saldoAtual: Double): DreamProgressUi {
    val progresso = if (targetAmount <= 0.0) 0f else (saldoAtual / targetAmount).coerceIn(0.0, 1.0).toFloat()
    val status = when {
        saldoAtual >= targetAmount -> DreamStatus.ATINGIVEL
        saldoAtual > 0.0 -> DreamStatus.PARCIALMENTE_ATINGIVEL
        else -> DreamStatus.NAO_ATINGIVEL
    }

    return DreamProgressUi(
        id = id,
        nome = name,
        valorObjetivo = targetAmount,
        progresso = progresso,
        status = status
    )
}

