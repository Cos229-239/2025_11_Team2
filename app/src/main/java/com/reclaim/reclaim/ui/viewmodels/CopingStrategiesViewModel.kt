package com.reclaim.reclaim.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.reclaim.reclaim.data.db.AppDatabase
import com.reclaim.reclaim.data.entities.StrategyEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CopingStrategiesViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).strategyDao()
    private val _showFavoritesOnly = MutableStateFlow(false)
    val showFavoritesOnly = _showFavoritesOnly.asStateFlow()

    val strategies: Flow<List<StrategyEntity>> = combine(dao.getAllStrategies(), _showFavoritesOnly) { all, favoritesOnly ->
        if (favoritesOnly) all.filter { it.isFavorite } else all
    }

    val stats: Flow<Pair<Int, Int>> = dao.getAllStrategies().map { all ->
        Pair(all.size, all.count { it.isFavorite })
    }

    init {
        populateStrategiesIfEmpty()
    }

    private fun populateStrategiesIfEmpty() {
        viewModelScope.launch {
            if (dao.getAllStrategies().firstOrNull()?.isEmpty() == true) {
                val initialStrategies: List<StrategyEntity> = listOf(
                    // Your 20 strategies here...
                )
                dao.insertAll(initialStrategies)
            }
        }
    }

    fun toggleFavorite(strategy: StrategyEntity) {
        viewModelScope.launch {
            dao.update(strategy.copy(isFavorite = !strategy.isFavorite))
        }
    }

    fun toggleShowFavoritesOnly() {
        _showFavoritesOnly.value = !_showFavoritesOnly.value
    }

    fun getRandomStrategy(strategies: List<StrategyEntity>): StrategyEntity? {
        return if (strategies.isNotEmpty()) strategies.random() else null
    }

    fun addCustomStrategy(triggerName: String, strategy: String) {
        viewModelScope.launch {
            val newStrategy = StrategyEntity(triggerName = triggerName, strategy = strategy)
            dao.insertAll(listOf(newStrategy))
        }
    }
}