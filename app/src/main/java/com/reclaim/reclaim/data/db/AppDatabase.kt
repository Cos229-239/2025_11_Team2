package com.reclaim.reclaim.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.reclaim.reclaim.data.daos.MilestoneDao
import com.reclaim.reclaim.data.daos.MoodDao
import com.reclaim.reclaim.data.daos.StrategyDao
import com.reclaim.reclaim.data.daos.TriggerDao
import com.reclaim.reclaim.data.entities.MilestoneEntity
import com.reclaim.reclaim.data.entities.MoodEntry
import com.reclaim.reclaim.data.entities.StrategyEntity
import com.reclaim.reclaim.data.entities.TriggerEntity



@Database(entities = [MilestoneEntity::class, MoodEntry::class, StrategyEntity::class, TriggerEntity::class], version = 3)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun milestoneDao(): MilestoneDao
    abstract fun moodDao(): MoodDao
    abstract fun strategyDao(): StrategyDao
    abstract fun triggerDao(): TriggerDao



    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reclaim_database"
                ).fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}
