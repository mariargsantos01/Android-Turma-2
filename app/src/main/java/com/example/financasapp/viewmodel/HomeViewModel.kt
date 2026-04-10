package com.example.financasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financasapp.data.repository.FinanceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val saldoAtual: Double = 0.0,
    val totalGanhos: Double = 0.0,
    val totalGastos: Double = 0.0,
    val quantidadeSonhos: Int = 0
)

sealed class HomeEvent {
    data object Refresh : HomeEvent()
}

class HomeViewModel(
    private val repository: FinanceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.observeIncomes(),
                repository.observeExpenses(),
                repository.observeDreams()
            ) { incomes, expenses, dreams ->
                val totalGanhos = incomes.sumOf { it.amount }
                val totalGastos = expenses.sumOf { it.amount }
                HomeUiState(
                    saldoAtual = totalGanhos - totalGastos,
                    totalGanhos = totalGanhos,
                    totalGastos = totalGastos,
                    quantidadeSonhos = dreams.size
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.Refresh -> _uiState.update { it }
        }
    }
}

