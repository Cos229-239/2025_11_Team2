package com.reclaim.reclaim.data.db

import androidx.room.TypeConverter
import com.reclaim.reclaim.ui.trigger.TriggerType
import java.time.LocalDate

class Converters {


    @TypeConverter
    fun fromIntList(list: List<Int>?): String? {
        return list?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toIntList(data: String?): List<Int> {
        if (data.isNullOrBlank()) return emptyList()
        return data.split(",").mapNotNull { it.toIntOrNull() }
    }


    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(epochDay: Long?): LocalDate? {
        return epochDay?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun fromTriggerType(type: TriggerType?): String? {
        return type?.name
    }

    @TypeConverter
    fun toTriggerType(name: String?): TriggerType? {
        return name?.let {
            runCatching { TriggerType.valueOf(it) }.getOrNull()
        }
    }
}
