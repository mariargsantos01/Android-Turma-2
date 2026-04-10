package com.example.financasapp.data.local

import androidx.room.TypeConverter
import com.example.financasapp.data.model.EntryType

class Converters {
    @TypeConverter
    fun fromEntryType(value: EntryType): String = value.name

    @TypeConverter
    fun toEntryType(value: String): EntryType = EntryType.valueOf(value)
}

