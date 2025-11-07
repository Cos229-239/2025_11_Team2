package com.reclaim.reclaim.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE StrategyEntity ADD COLUMN isFavorite INTEGER NOT NULL DEFAULT 0")
    }
}