package com.reclaim.reclaim.data.db


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.reclaim.reclaim.data.daos.JournalDao
import com.reclaim.reclaim.data.daos.MilestoneDao
import com.reclaim.reclaim.data.daos.MoodDao
import com.reclaim.reclaim.data.daos.StrategyDao
import com.reclaim.reclaim.data.daos.TriggerDao
import com.reclaim.reclaim.data.daos.UserDao
import com.reclaim.reclaim.data.entities.JournalEntity
import com.reclaim.reclaim.data.entities.MilestoneEntity
import com.reclaim.reclaim.data.entities.MoodEntry
import com.reclaim.reclaim.data.entities.StrategyEntity
import com.reclaim.reclaim.data.entities.TriggerEntity
import com.reclaim.reclaim.data.entities.User

/**
 * AppDatabase
 * -----------
 * Central Room database definition.
 * - Declares all entities (Journal, Milestone, Mood, Strategy, Trigger).
 * - Provides DAOs for each entity.
 * - Singleton instance managed via getDatabase().
 */

@Database(entities = [JournalEntity::class, MilestoneEntity::class, MoodEntry::class, StrategyEntity::class, TriggerEntity::class, User::class], version = 7, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun milestoneDao(): MilestoneDao
    abstract fun moodDao(): MoodDao
    abstract fun strategyDao(): StrategyDao
    abstract fun triggerDao(): TriggerDao
    abstract fun journalDao(): JournalDao
    abstract fun userDao(): UserDao





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