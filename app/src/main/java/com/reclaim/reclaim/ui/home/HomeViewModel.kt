package com.reclaim.reclaim.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.reclaim.reclaim.data.affirmations
import com.reclaim.reclaim.data.db.AppDatabase
import com.reclaim.reclaim.data.entities.MoodEntry
import com.reclaim.reclaim.data.entities.MilestoneEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import com.reclaim.reclaim.model.SoberTime
import kotlinx.coroutines.flow.Flow

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val moodDao = AppDatabase.getDatabase(application).moodDao()
    private val milestoneDao = AppDatabase.getDatabase(application).milestoneDao()
    private val soberStartDate = LocalDate.of(2024, 8, 25)
    private val milestoneDays = listOf(1L, 3L, 7L, 30L, 60L, 90L, 180L, 365L, 730L)

    private val _soberTime = MutableStateFlow(calculateSoberTime(soberStartDate))
    val soberTime: StateFlow<SoberTime> = _soberTime

    val affirmationsList = affirmations
    val affirmation = MutableStateFlow("")
    val mood = MutableStateFlow<String?>(null)
    val milestoneReached = MutableStateFlow<Long?>(null)

    val moodHistory: Flow<List<MoodEntry>> = moodDao.getAllMoods()
    val weeklyMoodHistory: Flow<List<MoodEntry>> = moodDao.getMoodsSince(LocalDate.now().minusDays(6))

    init {
        val todayIndex = LocalDate.now().dayOfYear % affirmations.size
        affirmation.value = affirmations[todayIndex]

        viewModelScope.launch {
            val today = LocalDate.now()
            val saved = moodDao.getMoodByDate(today)
            mood.value = saved?.mood
            refreshSoberTime() // ✅ recalc and check milestone
        }
    }

    fun setMood(selectedMood: String) {
        mood.value = selectedMood
        viewModelScope.launch {
            val today = LocalDate.now()
            moodDao.insertMood(MoodEntry(mood = selectedMood, date = today))
        }
    }

    fun refreshSoberTime() {
        val updated = calculateSoberTime(soberStartDate)
        _soberTime.value = updated
        viewModelScope.launch { checkMilestone(updated) }
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

    // Debug helper(delete later when not need along with the debug code in HomeScreen.kt)
    fun triggerMilestone(days: Long) {
        milestoneReached.value = days
    }


}
