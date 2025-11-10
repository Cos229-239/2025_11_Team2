package com.reclaim.reclaim.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.reclaim.reclaim.data.daos.StrategyDao
import com.reclaim.reclaim.data.entities.StrategyEntity

@Database(entities = [StrategyEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun strategyDao(): StrategyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reclaim_database"
                )
                    .addMigrations(MIGRATION_1_2) // Reference from separate file
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
