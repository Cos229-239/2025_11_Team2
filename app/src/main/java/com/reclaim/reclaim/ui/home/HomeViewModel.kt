package com.reclaim.reclaim.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class SoberTime(val years: Int, val months: Int, val days: Int)

class HomeViewModel : ViewModel() {
    private val _soberTime = MutableStateFlow(SoberTime(0, 0, 0))
    val soberTime: StateFlow<SoberTime> = _soberTime

    private val _affirmation = MutableStateFlow("You're stronger today than yesterday!")
    val affirmation: StateFlow<String> = _affirmation
}
