package com.example.financasapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.financasapp.data.model.DreamEntity
import com.example.financasapp.data.model.EntryEntity
import com.example.financasapp.data.model.EntryType
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {
    @Query("SELECT * FROM entries WHERE type = :type ORDER BY year DESC, month DESC, id DESC")
    fun observeEntriesByType(type: EntryType): Flow<List<EntryEntity>>

    @Query("SELECT * FROM entries")
    fun observeAllEntries(): Flow<List<EntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEntry(entry: EntryEntity)

    @Query("DELETE FROM entries WHERE id = :id")
    suspend fun deleteEntryById(id: Long)

    @Query("SELECT * FROM dreams ORDER BY id DESC")
    fun observeDreams(): Flow<List<DreamEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDream(dream: DreamEntity)

    @Query("DELETE FROM dreams WHERE id = :id")
    suspend fun deleteDreamById(id: Long)
}

