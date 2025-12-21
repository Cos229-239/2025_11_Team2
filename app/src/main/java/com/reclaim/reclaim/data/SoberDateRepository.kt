package com.reclaim.reclaim.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.reclaim.reclaim.ui.viewmodels.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject

class SoberDateRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore
    private val soberDateKey = stringPreferencesKey("sober_date")

    suspend fun saveSoberDate(date: LocalDate): Result<Unit> {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return Result.failure(Exception("User not logged in"))
        return try {
            val doc = FirebaseFirestore.getInstance().collection("users").document(uid)
            doc.set(mapOf("soberStart" to date.toString()), com.google.firebase.firestore.SetOptions.merge()).await()
            dataStore.edit { prefs -> prefs[soberDateKey] = date.toString() }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getSoberDate(): Flow<String?> {
        return dataStore.data.map { prefs -> prefs[soberDateKey] }
    }

    suspend fun syncFromFirestore(): Result<Unit> {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return Result.failure(Exception("User not logged in"))
        return try {
            val snapshot = FirebaseFirestore.getInstance().collection("users").document(uid).get().await()
            val date = snapshot.getString("soberStart")
            if (date != null) {
                dataStore.edit { prefs -> prefs[soberDateKey] = date }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
