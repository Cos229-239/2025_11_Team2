// C:/fullsail classes/2025_11_Team2/app/src/main/java/com/reclaim/reclaim/data/AppDataStore.kt
package com.reclaim.reclaim.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Define the DataStore instance as a top-level extension on Context
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


