package com.reclaim.reclaim.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.reclaim.reclaim.data.entities.TriggerEntity
import com.reclaim.reclaim.data.model.TriggerType
import java.time.LocalDate

/**
 * Converters
 * ----------
 * Room type converters for unsupported types.
 * - Converts List<Int> ↔ String
 * - Converts LocalDate ↔ Long (epochDay)
 * - Converts TriggerType enum ↔ String
 * - Converts TriggerEntity ↔ JSON String (via Gson)
 *
 * TODO:
 * - Add converters for other entities (MoodEntry, StrategyEntity, etc.)
 * - Consider switching to Kotlinx Serialization for type safety.
 */
class Converters {

    private val gson = Gson()

    // List<Int> ↔ String
    @TypeConverter
    fun fromIntList(list: List<Int>?): String? =
        list?.joinToString(",")

    @TypeConverter
    fun toIntList(data: String?): List<Int> =
        data?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()

    // LocalDate ↔ Long
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
