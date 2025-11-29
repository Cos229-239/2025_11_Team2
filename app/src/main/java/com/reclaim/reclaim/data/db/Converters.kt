package com.reclaim.reclaim.data.db

import androidx.room.TypeConverter
import com.reclaim.reclaim.ui.trigger.TriggerType
import java.time.LocalDate

class Converters {

    // --- LocalDate converters ---
    @TypeConverter
    fun fromLocalDate(date: LocalDate): String = date.toString()

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate = LocalDate.parse(dateString)

    // --- List<Int> converters ---
    @TypeConverter
    fun fromIntList(list: List<Int>): String = list.joinToString(",")

    // --- TriggerType converters ---
    @TypeConverter
    fun toIntList(data: String): List<Int> =
        if (data.isBlank()) emptyList()
        else data.split(",").map { it.toInt() }

    @TypeConverter
    fun fromTriggerType(type: TriggerType): String = type.name

    @TypeConverter
    fun toTriggerType(name: String): TriggerType = TriggerType.valueOf(name)
    }

