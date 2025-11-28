package com.reclaim.reclaim.ui.trigger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reclaim.reclaim.data.daos.TriggerDao
import com.reclaim.reclaim.data.entities.TriggerEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TriggerMapViewModel @Inject constructor(
    private val dao: TriggerDao
) : ViewModel() {

    // Expose triggers grouped by date, as TriggerType
    val triggers: Flow<Map<LocalDate, List<TriggerType>>> =
        dao.getAllTriggers().map { list ->
            list.groupBy { it.date }.mapValues { entry ->
                entry.value.map { it.type }
            }
        }

    // Log a new trigger
    fun logTrigger(date: LocalDate, type: TriggerType) {
        viewModelScope.launch {
            dao.insert(
                TriggerEntity(
                    date = date,
                    type = type,
                    description = null,
                    linkedStrategyIds = emptyList()
                )
            )
        }
    }

    // Optional: delete trigger
    fun deleteTrigger(trigger: TriggerEntity) {
        viewModelScope.launch {
            dao.delete(trigger)
        }
    }
}
