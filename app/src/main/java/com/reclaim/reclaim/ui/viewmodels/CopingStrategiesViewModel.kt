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

    // NEW: Stats counter for total and favorites count
    val stats: Flow<Pair<Int, Int>> = dao.getAllStrategies().map { all ->
        all.size to all.count { it.isFavorite }  // Pair for counts (total, favorites)
    }

    init {
        populateStrategiesIfEmpty()
    }

    private fun populateStrategiesIfEmpty() {
        viewModelScope.launch {
            if (dao.getAllStrategies().firstOrNull()?.isEmpty() == true) {
                val initialStrategies = listOf(
                    StrategyEntity(triggerName = "Practice Self-Care", strategy = "Prioritize physical, emotional, and mental well-being through balanced diet, adequate sleep, hygiene, and joyful activities to build resilience and reduce stress."),
                    StrategyEntity(triggerName = "Use Stress Management Techniques", strategy = "Employ deep breathing, yoga, meditation, or relaxation exercises to handle anxiety and promote calm, which can prevent cravings from escalating."),
                    StrategyEntity(triggerName = "Build a Support Network", strategy = "Connect with friends, family, sponsors, or groups like AA/NA for accountability, encouragement, and to combat loneliness—a common trigger."),
                    StrategyEntity(triggerName = "Identify and Manage Triggers", strategy = "Recognize people, places, emotions, or situations that spark cravings, then avoid them or use distraction/distancing strategies to stay safe."),
                    StrategyEntity(triggerName = "Exercise Regularly", strategy = "Engage in cardio, strength training, or active hobbies (e.g., walking) to improve mood, sleep, self-esteem, and reduce withdrawal symptoms—aim for 150 minutes weekly."),
                    StrategyEntity(triggerName = "Practice Mindfulness and Meditation", strategy = "Focus on the present moment non-judgmentally to better handle cravings, pain, depression, and anxiety."),
                    StrategyEntity(triggerName = "Journal Daily", strategy = "Write about emotions, gratitude, or daily plans to process stress, track progress, and stay grounded in the moment."),
                    StrategyEntity(triggerName = "Set Healthy Boundaries", strategy = "Learn to say \"no\" to enabling people or risky situations to protect your recovery."),
                    StrategyEntity(triggerName = "Use Positive Reinforcement", strategy = "Celebrate milestones with rewards to boost motivation and self-esteem."),
                    StrategyEntity(triggerName = "Develop Problem-Solving Skills", strategy = "Break challenges into steps, brainstorm solutions, and seek input to handle issues without substances."),
                    StrategyEntity(triggerName = "Practice Cognitive Restructuring", strategy = "Challenge negative thoughts and replace them with realistic, positive ones to avoid self-destructive patterns."),
                    StrategyEntity(triggerName = "Create a Relapse Prevention Plan", strategy = "Outline early warning signs, action steps, and emergency contacts for if cravings intensify."),
                    StrategyEntity(triggerName = "Check HALT (Hungry, Angry, Lonely, Tired)", strategy = "Regularly assess these states and address them promptly to prevent them from triggering relapse."),
                    StrategyEntity(triggerName = "Use Grounding Techniques", strategy = "Try the 5-4-3-2-1 method (name 5 things you see, 4 you can touch, etc.) to stay present during anxiety."),
                    StrategyEntity(triggerName = "Play the Tape Through", strategy = "Visualize the full consequences of relapsing to deter impulsive actions."),
                    StrategyEntity(triggerName = "Be Honest with Yourself and Others", strategy = "Express feelings openly to build trust and avoid isolation."),
                    StrategyEntity(triggerName = "Engage in Enjoyable Activities", strategy = "Fill time with hobbies like gardening, cooking, or learning new skills to combat boredom."),
                    StrategyEntity(triggerName = "Establish a Healthy Sleep Routine", strategy = "Aim for 7-9 hours nightly with consistent habits to support overall recovery."),
                    StrategyEntity(triggerName = "Help Others", strategy = "Volunteer or sponsor someone to boost your own resilience and sense of purpose."),
                    StrategyEntity(triggerName = "Pause Before Reacting", strategy = "Take a breath during triggers to avoid impulsivity and make better choices.")
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
            // Create new custom strategies
            val newStrategy = StrategyEntity(triggerName = triggerName, strategy = strategy)
            dao.insertAll(listOf(newStrategy))
        }
    }
}