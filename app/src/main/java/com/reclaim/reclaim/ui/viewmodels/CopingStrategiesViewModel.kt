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
import kotlinx.coroutines.launch


class CopingStrategiesViewModel(application: Application) : AndroidViewModel(application) {
    private val strategyDao = AppDatabase.getDatabase(application).strategyDao()

    private val _showFavoritesOnly = MutableStateFlow(false) // New: State for favorites filter
    val showFavoritesOnly = _showFavoritesOnly.asStateFlow() // New: Public read-only flow

    val strategies: Flow<List<StrategyEntity>> = combine(strategyDao.getAllStrategies(), _showFavoritesOnly) { all, favoritesOnly ->
        if (favoritesOnly) all.filter { strategy -> strategy.isFavorite } else all // New: Reactive filtering in Flow
    }

    init {
        populateStrategiesIfEmpty()
    }

    private fun populateStrategiesIfEmpty() {
        viewModelScope.launch {
            if (strategyDao.getAllStrategies().firstOrNull()?.isEmpty() == true) {
                val initialStrategies = listOf(
                    StrategyEntity(triggerName = "General Stress", strategy = "Prioritize physical, emotional, and mental well-being through balanced diet, adequate sleep, hygiene, and joyful activities to build resilience and reduce stress."),
                    StrategyEntity(triggerName = "Anxiety", strategy = "Employ deep breathing, yoga, meditation, or relaxation exercises to handle anxiety and promote calm, which can prevent cravings from escalating."),
                    StrategyEntity(triggerName = "Loneliness", strategy = "Connect with friends, family, sponsors, or groups like AA/NA for accountability, encouragement, and to combat loneliness—a common trigger."),
                    StrategyEntity(triggerName = "Cravings", strategy = "Recognize people, places, emotions, or situations that spark cravings, then avoid them or use distraction/distancing strategies to stay safe."),
                    StrategyEntity(triggerName = "Low Energy", strategy = "Engage in cardio, strength training, or active hobbies (e.g., walking) to improve mood, sleep, self-esteem, and reduce withdrawal symptoms—aim for 150 minutes weekly."),
                    StrategyEntity(triggerName = "Negative Emotions", strategy = "Focus on the present moment non-judgmentally to better handle cravings, pain, depression, and anxiety."),
                    StrategyEntity(triggerName = "Daily Reflection", strategy = "Write about emotions, gratitude, or daily plans to process stress, track progress, and stay grounded in the moment."),
                    StrategyEntity(triggerName = "Enabling Situations", strategy = "Learn to say \"no\" to enabling people or risky situations to protect your recovery."),
                    StrategyEntity(triggerName = "Milestones", strategy = "Celebrate milestones with rewards to boost motivation and self-esteem."),
                    StrategyEntity(triggerName = "Challenges", strategy = "Break challenges into steps, brainstorm solutions, and seek input to handle issues without substances."),
                    StrategyEntity(triggerName = "Negative Thoughts", strategy = "Challenge negative thoughts and replace them with realistic, positive ones to avoid self-destructive patterns."),
                    StrategyEntity(triggerName = "Craving Intensification", strategy = "Outline early warning signs, action steps, and emergency contacts for if cravings intensify."),
                    StrategyEntity(triggerName = "HALT States", strategy = "Regularly assess these states and address them promptly to prevent them from triggering relapse."),
                    StrategyEntity(triggerName = "Anxiety Attacks", strategy = "Try the 5-4-3-2-1 method (name 5 things you see, 4 you can touch, etc.) to stay present during anxiety."),
                    StrategyEntity(triggerName = "Impulsive Urges", strategy = "Visualize the full consequences of relapsing to deter impulsive actions."),
                    StrategyEntity(triggerName = "Isolation", strategy = "Express feelings openly to build trust and avoid isolation."),
                    StrategyEntity(triggerName = "Boredom", strategy = "Fill time with hobbies like gardening, cooking, or learning new skills to combat boredom."),
                    StrategyEntity(triggerName = "Sleep Issues", strategy = "Aim for 7-9 hours nightly with consistent habits to support overall recovery."),
                    StrategyEntity(triggerName = "Lack of Purpose", strategy = "Volunteer or sponsor someone to boost your own resilience and sense of purpose."),
                    StrategyEntity(triggerName = "Triggers", strategy = "Take a breath during triggers to avoid impulsivity and make better choices.")
                )
                strategyDao.insertAll(initialStrategies)
            }
        }
    }

    fun toggleFavorite(strategy: StrategyEntity) { // New: Toggle favorite in DB
        viewModelScope.launch {
            strategyDao.update(strategy.copy(isFavorite = !strategy.isFavorite))
        }
    }

    fun toggleShowFavoritesOnly() { // New: Toggle the favorites filter state
        _showFavoritesOnly.value = !_showFavoritesOnly.value
    }
}