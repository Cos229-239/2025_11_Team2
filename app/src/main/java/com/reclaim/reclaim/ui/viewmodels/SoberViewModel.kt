package com.reclaim.reclaim.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reclaim.reclaim.data.SoberDateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SoberViewModel @Inject constructor(
    private val soberDateRepository: SoberDateRepository
) : ViewModel() {

    // Reactive flow of the sober date string (ISO format)
    val soberDate: StateFlow<String?> = soberDateRepository.getSoberDate().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    // Save sober date to Firestore and local DataStore
    fun saveSoberDate(date: LocalDate) {
        viewModelScope.launch {
            val result = soberDateRepository.saveSoberDate(date)
            result.onFailure { e ->
                // Optionally expose error to UI via another StateFlow
                println("Failed to save sober date: ${e.message}")
            }
        }
    }

    // Sync sober date from Firestore into local DataStore
    fun syncSoberDate() {
        viewModelScope.launch {
            val result = soberDateRepository.syncFromFirestore()
            result.onFailure { e ->
                println("Failed to sync sober date: ${e.message}")
            }
        }
    }
}
