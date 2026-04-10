package com.example.financasapp.data.repository

import com.example.financasapp.data.local.FinanceDao
import com.example.financasapp.data.model.Dream
import com.example.financasapp.data.model.DreamEntity
import com.example.financasapp.data.model.EntryEntity
import com.example.financasapp.data.model.EntryType
import com.example.financasapp.data.model.Expense
import com.example.financasapp.data.model.Income
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FinanceRepositoryImpl(
    private val dao: FinanceDao
) : FinanceRepository {

    override fun observeIncomes(): Flow<List<Income>> {
        return dao.observeEntriesByType(EntryType.INCOME).map { list ->
            list.map { entity ->
                Income(
                    id = entity.id,
                    amount = entity.amount,
                    description = entity.description,
                    month = entity.month,
                    year = entity.year
                )
            }
        }
    }

    override fun observeExpenses(): Flow<List<Expense>> {
        return dao.observeEntriesByType(EntryType.EXPENSE).map { list ->
            list.map { entity ->
                Expense(
                    id = entity.id,
                    amount = entity.amount,
                    description = entity.description,
                    month = entity.month,
                    year = entity.year
                )
            }
        }
    }

    override fun observeDreams(): Flow<List<Dream>> {
        return dao.observeDreams().map { list ->
            list.map { entity ->
                Dream(
                    id = entity.id,
                    name = entity.name,
                    targetAmount = entity.targetAmount
                )
            }
        }
    }

    override suspend fun upsertIncome(income: Income) {
        dao.upsertEntry(
            EntryEntity(
                id = income.id,
                type = EntryType.INCOME,
                amount = income.amount,
                description = income.description,
                month = income.month,
                year = income.year
            )
        )
    }

    override suspend fun upsertExpense(expense: Expense) {
        dao.upsertEntry(
            EntryEntity(
                id = expense.id,
                type = EntryType.EXPENSE,
                amount = expense.amount,
                description = expense.description,
                month = expense.month,
                year = expense.year
            )
        )
    }

    override suspend fun upsertDream(dream: Dream) {
        dao.upsertDream(
            DreamEntity(
                id = dream.id,
                name = dream.name,
                targetAmount = dream.targetAmount
            )
        )
    }

    override suspend fun deleteIncome(id: Long) {
        dao.deleteEntryById(id)
    }

    override suspend fun deleteExpense(id: Long) {
        dao.deleteEntryById(id)
    }

    override suspend fun deleteDream(id: Long) {
        dao.deleteDreamById(id)
    }
}

