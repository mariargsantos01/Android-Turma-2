package com.example.financasapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entries")
data class EntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: EntryType,
    val amount: Double,
    val description: String,
    val month: Int,
    val year: Int
)

