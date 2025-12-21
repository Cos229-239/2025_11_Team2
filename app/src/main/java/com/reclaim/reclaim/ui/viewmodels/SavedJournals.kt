package com.reclaim.reclaim.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.reclaim.reclaim.data.AuthRepository
// Import both data classes to perform the mapping
import com.reclaim.reclaim.data.JournalEntry // UI Model
//import com.reclaim.reclaim.data.JournalEntity // Data Model
import com.reclaim.reclaim.data.entities.JournalEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map // <-- Import the map operator
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import java.time.Instant


@HiltViewModel
class SavedJournals @Inject constructor(
    private val authRepository: AuthRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val currentUserId: String? get() = auth.currentUser?.uid

    val entries: StateFlow<List<JournalEntry>> = currentUserId?.let { uid ->
        authRepository.getJournalEntries(uid)
            .map { entityList -> // <-- Use the map operator to transform the list
                entityList.map { entity ->
                    // Convert each JournalEntity to a JournalEntry
                    JournalEntry(
                        id = entity.id.toString(),
                        text = entity.text,
                        date = entity.date,
                        time = entity.time
                        // Make sure all necessary fields are mapped
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )
    } ?: emptyFlow<List<JournalEntry>>().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    // ... rest of your ViewModel code
    fun addEntry(text: String, date: String) {
        val userId = currentUserId ?: return
        if (text.isBlank()) return

        viewModelScope.launch {
            val time = LocalTime.now().format(DateTimeFormatter.ofPattern("h:mm a"))
            // MUST create a JournalEntry object
            val newEntry = JournalEntry(
                text = text.trim(),
                date = date,
                time = time
            )
            // This sends the correct object to the repository
            authRepository.addJournalEntry(userId, newEntry)
        }
    }

    fun deleteEntry(entryId: String) {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            authRepository.deleteJournalEntry(userId, entryId)
        }
    }
}
