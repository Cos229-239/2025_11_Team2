package com.reclaim.reclaim.di

import android.content.Context
import androidx.room.Room
import com.reclaim.reclaim.data.db.AppDatabase
import com.reclaim.reclaim.data.daos.MoodDao
import com.reclaim.reclaim.data.daos.MilestoneDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DatabaseModule
 * --------------
 * Provides Room database and DAO dependencies via Hilt.
 * - Singleton AppDatabase instance
 * - MoodDao and MilestoneDao bindings
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "reclaim_database"
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideMoodDao(db: AppDatabase): MoodDao = db.moodDao()

    @Provides
    fun provideMilestoneDao(db: AppDatabase): MilestoneDao = db.milestoneDao()
}
