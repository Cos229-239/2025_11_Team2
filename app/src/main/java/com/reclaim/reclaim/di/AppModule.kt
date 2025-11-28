package com.reclaim.reclaim.di

import android.content.Context
import androidx.room.Room
import com.reclaim.reclaim.data.db.AppDatabase
import com.reclaim.reclaim.data.daos.TriggerDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * AppModule
 * ---------
 * Hilt DI module providing database + DAO instances.
 * - Installed in SingletonComponent (application-wide scope).
 * - Ensures single AppDatabase instance.
 * - Exposes TriggerDao for injection into ViewModels.
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "reclaim_database"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTriggerDao(db: AppDatabase): TriggerDao = db.triggerDao()
}
