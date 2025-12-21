package com.reclaim.reclaim.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.reclaim.reclaim.data.entities.TriggerEntity
import com.reclaim.reclaim.data.model.TriggerType
import java.time.LocalDate
import java.util.Date // <-- Import java.util.Date for the new converter

// FIX: Changed from 'annotation class' to a regular 'class'.
// This is the correct way to define a collection of converters for Room.
class Converters {

    private val gson = Gson() // It's better to make this a private val

    // --- Converter for java.util.Date ---
    // This was missing and is needed for the 'createdAt' field in JournalEntity.
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        // Converts a Long from the database back into a Date object.
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        // Converts a Date object into a Long (milliseconds) to be stored in the database.
        return date?.time
    }
    // ------------------------------------

    // --- Your Existing Converters (They are correct) ---

    // List<Int> ↔ String
    @TypeConverter
    fun fromIntList(list: List<Int>?): String? =
        list?.joinToString(",")

    @TypeConverter
    fun toIntList(data: String?): List<Int> =
        data?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()

    // LocalDate ↔ Long (epochDay)
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? =
        date?.toEpochDay()

    @TypeConverter
    fun toLocalDate(epochDay: Long?): LocalDate? =
        epochDay?.let { LocalDate.ofEpochDay(it) }

    // TriggerType enum ↔ String
    @TypeConverter
    fun fromTriggerType(type: TriggerType?): String? =
        type?.name

    @TypeConverter
    fun toTriggerType(name: String?): TriggerType? =
        name?.let { runCatching { TriggerType.valueOf(it) }.getOrNull() }

    // TriggerEntity ↔ JSON String
    @TypeConverter
    fun fromTriggerEntity(entity: TriggerEntity?): String? =
        entity?.let { gson.toJson(it) }

    @TypeConverter
    fun toTriggerEntity(data: String?): TriggerEntity? =
        data?.let { gson.fromJson(it, TriggerEntity::class.java) }
}
