package com.reclaim.reclaim.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class AppDatabase : RoomDatabase() {

    companion object {

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reclaim_database"
            }
        }
    }
}
