package com.reclaim.reclaim.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration // New import for the upgrade tool
import androidx.sqlite.db.SupportSQLiteDatabase // New import for the database helper
import com.reclaim.reclaim.data.daos.StrategyDao
import com.reclaim.reclaim.data.entities.StrategyEntity

@Database(entities = [StrategyEntity::class], version = 2, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {
    abstract fun strategyDao(): StrategyDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // New: Set up the instructions to update the database from old version 1 to new version 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE StrategyEntity ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reclaim_database"
                )
                    .addMigrations(MIGRATION_1_2) // New: Tell the database builder to use the update instructions
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}