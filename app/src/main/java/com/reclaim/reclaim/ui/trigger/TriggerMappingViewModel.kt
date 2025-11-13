package com.reclaim.reclaim.ui.trigger

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.reclaim.reclaim.data.db.AppDatabase
import com.reclaim.reclaim.data.entities.TriggerEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class TriggerMappingViewModel(application: Application) : AndroidViewModel(application) {
    private val triggerDao = AppDatabase.getDatabase(application).triggerDao()
    private val strategyDao = AppDatabase.getDatabase(application).strategyDao()

    val triggers: Flow<List<TriggerEntity>> = triggerDao.getAllTriggers()

    fun linkStrategyToTrigger(triggerId: Int, strategyId: Int) {
        viewModelScope.launch {
            val trigger = triggerDao.getAllTriggers().first().find { it.id == triggerId }
            if (trigger != null) {
                val updated = trigger.copy(linkedStrategyIds = trigger.linkedStrategyIds + strategyId)
                triggerDao.update(updated)
            }
        }
    }
}
