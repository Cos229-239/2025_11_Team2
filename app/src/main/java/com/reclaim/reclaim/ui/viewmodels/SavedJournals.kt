package com.reclaim.reclaim.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.reclaim.reclaim.data.db.AppDatabase
import com.reclaim.reclaim.data.entities.JournalEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class SavedJournals(application: Application) : AndroidViewModel(application) {
    private val journalDao = AppDatabase.getDatabase(application).journalDao()

    val entries: Flow<List<JournalEntity>> = journalDao.getAllJournals()

    fun addEntry(text: String, date: String) {
        if (text.isBlank()) return
        val time = LocalTime.now().format(DateTimeFormatter.ofPattern("h:mm a"))
        viewModelScope.launch {
            journalDao.insertJournal(JournalEntity(text = text.trim(), date = date, time = time))
        }
    }

    fun deleteEntry(id: Int) {
        viewModelScope.launch { journalDao.deleteJournal(id) }
    }
}
