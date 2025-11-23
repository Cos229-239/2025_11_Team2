package com.reclaim.reclaim.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.reclaim.reclaim.model.JournalEntry
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class SavedJournals : ViewModel() {

    private val _entries = MutableStateFlow<List<JournalEntry>>(emptyList())
    val entries: StateFlow<List<JournalEntry>> = _entries.asStateFlow()

    fun addEntry(text: String, date: String) {
        if (text.isBlank()) return

        val time = LocalTime.now().format(
            DateTimeFormatter.ofPattern("h:mm a")
        )

        _entries.update { current ->
            current + JournalEntry(
                text = text,
                date = date,
                time = time
            )
        }
    }
}