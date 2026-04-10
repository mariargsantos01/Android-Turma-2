package com.example.financasapp.data.repository

import com.example.financasapp.data.model.Dream
import com.example.financasapp.data.model.Expense
import com.example.financasapp.data.model.Income
import kotlinx.coroutines.flow.Flow

interface FinanceRepository {
    fun observeIncomes(): Flow<List<Income>>
    fun observeExpenses(): Flow<List<Expense>>
    fun observeDreams(): Flow<List<Dream>>

    suspend fun upsertIncome(income: Income)
    suspend fun upsertExpense(expense: Expense)
    suspend fun upsertDream(dream: Dream)

    suspend fun deleteIncome(id: Long)
    suspend fun deleteExpense(id: Long)
    suspend fun deleteDream(id: Long)
}

