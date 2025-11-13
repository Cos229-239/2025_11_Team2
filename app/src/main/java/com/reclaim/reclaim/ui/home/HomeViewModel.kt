package com.reclaim.reclaim.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.Period
import java.time.temporal.ChronoUnit
import com.reclaim.reclaim.model.SoberTime



class HomeViewModel : ViewModel() {

    // Set your sober start date here
    private val soberStartDate = LocalDate.of(2024, 8, 25)

    // Reactive flow that calculates sober time
    val soberTime: StateFlow<SoberTime> = flow {
        val today = LocalDate.now()
        val period = Period.between(soberStartDate, today)
        val totalDays = ChronoUnit.DAYS.between(soberStartDate, today)

        emit(
            SoberTime(
                years = period.years,
                months = period.months,
                days = period.days,
                totalDays = totalDays
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = SoberTime(0, 0, 0, 0)
    )

    // Default affirmation
    private val _affirmation = MutableStateFlow("You're stronger today than yesterday!")
    val affirmation: StateFlow<String> = _affirmation.asStateFlow()
}