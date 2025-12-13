package com.reclaim.reclaim.ui.viewmodels

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reclaim.reclaim.data.AuthRepository
import com.reclaim.reclaim.data.affirmations
import com.reclaim.reclaim.data.daos.MilestoneDao
import com.reclaim.reclaim.data.daos.MoodDao
import com.reclaim.reclaim.data.entities.MilestoneEntity
import com.reclaim.reclaim.data.entities.MoodEntry
import com.reclaim.reclaim.model.SoberTime
import com.reclaim.reclaim.ui.widget.SoberTimeWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

val Context.dataStore by preferencesDataStore(name = "settings")

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val moodDao: MoodDao,
    private val milestoneDao: MilestoneDao,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val milestoneDays = listOf(1L, 3L, 7L, 30L, 60L, 90L, 180L, 365L, 730L)

    object PreferencesKeys {
        val SOBER_START_DATE = longPreferencesKey("sober_start_date")
    }

    // Flow of sober start date from DataStore
    private val soberStartDateFlow: Flow<LocalDate?> =
        appContext.dataStore.data.map { prefs ->
            prefs[PreferencesKeys.SOBER_START_DATE]?.let { LocalDate.ofEpochDay(it) }
        }

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
        affirmation.value = affirmations[todayIndex]

        viewModelScope.launch {
            val today = LocalDate.now()
            val saved = moodDao.getMoodByDate(today)
            mood.value = saved?.mood

            soberStartDateFlow.collect { startDate ->
                val effectiveDate = startDate ?: LocalDate.now()
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

    fun saveSoberStart(context: Context, date: LocalDate) {
        viewModelScope.launch {
            val uid = authRepository.currentUid() ?: return@launch
            val result = authRepository.updateSoberStart(uid, date)
            result.onSuccess {
                context.dataStore.edit { prefs ->
                    prefs[PreferencesKeys.SOBER_START_DATE] = date.toEpochDay()
                }
                SoberTimeWidget().updateAll(context)
            }
        }
    }

    private fun calculateSoberTime(startDate: LocalDate): SoberTime {
        val now = LocalDate.now()
        val totalDays = ChronoUnit.DAYS.between(startDate, now)
        val years = totalDays / 365
        val months = (totalDays % 365) / 30
        val days = (totalDays % 365) % 30
        return SoberTime(years.toInt(), months.toInt(), days.toInt(), totalDays)
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

    fun triggerMilestone(days: Long) {
        milestoneReached.value = days
    }



}