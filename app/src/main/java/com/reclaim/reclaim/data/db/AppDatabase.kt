package com.reclaim.reclaim.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reclaim.reclaim.data.daos.MilestoneDao
import com.reclaim.reclaim.data.daos.MoodDao
import com.reclaim.reclaim.data.entities.MilestoneEntity
import com.reclaim.reclaim.data.entities.MoodEntry

@Database(entities = [MilestoneEntity::class, MoodEntry::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun milestoneDao(): MilestoneDao
    abstract fun moodDao(): MoodDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reclaim_database"
                ).fallbackToDestructiveMigration() // Optional: handles version bumps
                    .build().also { INSTANCE = it }
            }
        }
    }
}
