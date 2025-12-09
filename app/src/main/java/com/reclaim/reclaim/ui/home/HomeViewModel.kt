package com.reclaim.reclaim.ui.viewmodels

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import android.content.Context
import com.reclaim.reclaim.data.affirmations
import com.reclaim.reclaim.data.db.AppDatabase
import com.reclaim.reclaim.data.entities.MilestoneEntity
import com.reclaim.reclaim.data.entities.MoodEntry
import com.reclaim.reclaim.model.SoberTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit

// DataStore extension
val Context.dataStore by preferencesDataStore(name = "settings")

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val moodDao = AppDatabase.getDatabase(application).moodDao()
    private val milestoneDao = AppDatabase.getDatabase(application).milestoneDao()
    private val milestoneDays = listOf(1L, 3L, 7L, 30L, 60L, 90L, 180L, 365L, 730L)

    object PreferencesKeys {
        val SOBER_START_DATE = longPreferencesKey("sober_start_date")
    }

    // Flow of sober start date from DataStore
    private val soberStartDateFlow: Flow<LocalDate?> =
        application.dataStore.data.map { prefs ->
            prefs[PreferencesKeys.SOBER_START_DATE]?.let {
                LocalDate.ofEpochDay(it)
            }
        }

    // Expose sober time as a StateFlow
    private val _soberTime = MutableStateFlow(SoberTime(0, 0, 0, 0))
    val soberTime: StateFlow<SoberTime> = _soberTime

    val affirmationsList = affirmations
    val affirmation = MutableStateFlow("")
    val mood = MutableStateFlow<String?>(null)
    val milestoneReached = MutableStateFlow<Long?>(null)

    val moodHistory: Flow<List<MoodEntry>> = moodDao.getAllMoods()
    val weeklyMoodHistory: Flow<List<MoodEntry>> =
        moodDao.getMoodsSince(LocalDate.now().minusDays(6))

    init {
        val todayIndex = LocalDate.now().dayOfYear % affirmations.size
        affirmation.value = affirmations[todayIndex].toString()

        viewModelScope.launch {
            val today = LocalDate.now()
            val saved = moodDao.getMoodByDate(today)
            mood.value = saved?.mood

            // Observe sober start date and recalc sober time
            soberStartDateFlow.collect { startDate ->
                val effectiveDate = startDate ?: LocalDate.now() // fallback
                val updated = calculateSoberTime(effectiveDate)
                _soberTime.value = updated
                checkMilestone(updated)
            }
        }
    }

    fun setMood(selectedMood: String) {
        mood.value = selectedMood
        viewModelScope.launch {
            val today = LocalDate.now()
            moodDao.insertMood(MoodEntry(mood = selectedMood, date = today))
        }
    }

    fun saveSoberStart(date: LocalDate) {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { prefs ->
                prefs[PreferencesKeys.SOBER_START_DATE] = date.toEpochDay()
            }
        }
    }


    private fun calculateSoberTime(startDate: LocalDate): SoberTime {
        val now = LocalDate.now()
        val totalDays = ChronoUnit.DAYS.between(startDate, now)

        val years = totalDays / 365
        val months = (totalDays % 365) / 30
        val days = (totalDays % 365) % 30

        return SoberTime(
            years = years.toInt(),
            months = months.toInt(),
            days = days.toInt(),
            totalDays = totalDays
        )
    }

    private suspend fun checkMilestone(soberTime: SoberTime) {
        val today = LocalDate.now()
        if (soberTime.totalDays in milestoneDays) {
            val already = milestoneDao.getMilestoneByDay(soberTime.totalDays)
            if (already == null) {
                milestoneDao.insertMilestone(
                    MilestoneEntity(dayCount = soberTime.totalDays, dateReached = today)
                )
                milestoneReached.value = soberTime.totalDays
            }
        } else {
            milestoneReached.value = null
        }
    }

    // Debug helper
    fun triggerMilestone(days: Long) {
        milestoneReached.value = days
    }
}