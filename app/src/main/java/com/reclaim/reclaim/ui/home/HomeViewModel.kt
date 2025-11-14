package com.reclaim.reclaim.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.reclaim.reclaim.data.db.AppDatabase
import com.reclaim.reclaim.data.entities.MoodEntry
import com.reclaim.reclaim.data.entities.MilestoneEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import com.reclaim.reclaim.model.SoberTime

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val moodDao = AppDatabase.getDatabase(application).moodDao()
    private val milestoneDao = AppDatabase.getDatabase(application).milestoneDao()

    private val soberStartDate = LocalDate.of(2024, 8, 25)
    private val milestoneDays = listOf(1L, 3L, 7L, 30L, 60L, 90L, 180L, 365L, 730L)

    private val _soberTime = MutableStateFlow(calculateSoberTime(soberStartDate))
    val soberTime: StateFlow<SoberTime> = _soberTime

    val affirmation = MutableStateFlow("You are strong and capable.")
    val mood = MutableStateFlow<String?>(null)
    val milestoneReached = MutableStateFlow<Long?>(null)

    init {
        viewModelScope.launch {
            val today = LocalDate.now()
            val saved = moodDao.getMoodByDate(today)
            mood.value = saved?.mood
            checkMilestone(_soberTime.value)
        }
    }

    fun setMood(selectedMood: String) {
        mood.value = selectedMood
        viewModelScope.launch {
            val today = LocalDate.now()
            moodDao.insertMood(MoodEntry(mood = selectedMood, date = today))
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
            milestoneDao.insertMilestone(
                MilestoneEntity(dayCount = soberTime.totalDays, dateReached = today)
            )
            milestoneReached.value = soberTime.totalDays
        }
    }

}