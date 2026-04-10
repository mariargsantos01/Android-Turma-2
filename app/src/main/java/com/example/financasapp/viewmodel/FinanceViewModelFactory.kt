package com.example.financasapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.financasapp.data.repository.FinanceRepository

class FinanceViewModelFactory(
    private val repository: FinanceRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository) as T
            modelClass.isAssignableFrom(GanhosViewModel::class.java) -> GanhosViewModel(repository) as T
            modelClass.isAssignableFrom(GastosViewModel::class.java) -> GastosViewModel(repository) as T
            modelClass.isAssignableFrom(SonhosViewModel::class.java) -> SonhosViewModel(repository) as T
            else -> throw IllegalArgumentException("ViewModel desconhecido: ${modelClass.name}")
        }
    }
}

