package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.RizzDatabase
import com.example.data.model.FavoriteLine
import com.example.data.model.GenerationRequest
import com.example.data.model.HistoryItem
import com.example.data.model.PickupLine
import com.example.data.repository.RizzRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class RizzViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RizzRepository

    init {
        val db = RizzDatabase.getInstance(application)
        repository = RizzRepository(db.rizzDao())
    }

    val favorites: StateFlow<List<FavoriteLine>> = repository.allFavorites
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val history: StateFlow<List<HistoryItem>> = repository.allHistory
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Form inputs
    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _situation = MutableStateFlow("First time talking")
    val situation: StateFlow<String> = _situation.asStateFlow()

    private val _language = MutableStateFlow("Tanglish")
    val language: StateFlow<String> = _language.asStateFlow()

    private val _style = MutableStateFlow("Funny 😂")
    val style: StateFlow<String> = _style.asStateFlow()

    private val _confidence = MutableStateFlow("Casual")
    val confidence: StateFlow<String> = _confidence.asStateFlow()

    private val _count = MutableStateFlow(5)
    val count: StateFlow<Int> = _count.asStateFlow()

    // Screen navigation
    private val _currentScreen = MutableStateFlow("home")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    // Generated results
    private val _currentLines = MutableStateFlow<List<PickupLine>>(emptyList())
    val currentLines: StateFlow<List<PickupLine>> = _currentLines.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isDemoMode = MutableStateFlow(false)
    val isDemoMode: StateFlow<Boolean> = _isDemoMode.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    val situations = listOf(
        "First time talking",
        "Instagram DM",
        "WhatsApp chat",
        "College",
        "Friend",
        "Crush",
        "Dating app",
        "Birthday",
        "Compliment",
        "Funny conversation",
        "Random",
        "Custom"
    )

    val languages = listOf(
        "Tanglish",
        "English",
        "Tamil",
        "Tamil + English",
        "Malayalam",
        "Hindi"
    )

    val styles = listOf(
        "Funny 😂",
        "Cute 🥰",
        "Romantic ❤️",
        "Clever 🧠",
        "Confident 😎",
        "Flirty 😉",
        "Smooth ✨",
        "Sarcastic 😏",
        "Nerdy 🤓",
        "Short & Simple ⚡",
        "Wholesome 🌸"
    )

    val confidenceLevels = listOf(
        "Shy",
        "Casual",
        "Confident",
        "Bold"
    )

    fun updateName(value: String) {
        _name.value = value
    }

    fun updateSituation(value: String) {
        _situation.value = value
    }

    fun updateLanguage(value: String) {
        _language.value = value
    }

    fun updateStyle(value: String) {
        _style.value = value
    }

    fun updateConfidence(value: String) {
        _confidence.value = value
    }

    fun updateCount(value: Int) {
        _count.value = value
    }

    fun navigateTo(screen: String) {
        _currentScreen.value = screen
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun setToast(msg: String) {
        _toastMessage.value = msg
    }

    fun applyPreset(presetStyle: String, presetSituation: String = "Instagram DM", presetConfidence: String = "Confident") {
        _style.value = presetStyle
        _situation.value = presetSituation
        _confidence.value = presetConfidence
        _currentScreen.value = "generator"
        generateLines()
    }

    fun surpriseMe() {
        _style.value = styles.random(Random(System.currentTimeMillis()))
        _situation.value = situations.filter { it != "Custom" }.random(Random(System.currentTimeMillis() + 1))
        _confidence.value = confidenceLevels.random(Random(System.currentTimeMillis() + 2))
        _language.value = listOf("Tanglish", "English", "Tamil + English").random(Random(System.currentTimeMillis() + 3))
        _currentScreen.value = "generator"
        generateLines()
    }

    fun generateLines() {
        viewModelScope.launch {
            _isLoading.value = true
            val request = GenerationRequest(
                name = _name.value,
                situation = _situation.value,
                language = _language.value,
                style = _style.value,
                confidence = _confidence.value,
                count = _count.value
            )

            val result = repository.generateLines(request)
            _currentLines.value = result.lines
            _isDemoMode.value = result.isDemoMode
            _isLoading.value = false

            if (result.errorMessage != null && !result.isDemoMode) {
                _toastMessage.value = result.errorMessage
            } else if (result.isDemoMode) {
                _toastMessage.value = "✨ Generated via Rizz Engine"
            }
        }
    }

    fun toggleFavorite(line: PickupLine, isCurrentlyFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(line, isCurrentlyFav)
            _toastMessage.value = if (isCurrentlyFav) "Removed from favorites" else "Added to favorites ❤️"
        }
    }

    fun removeFavorite(id: String) {
        viewModelScope.launch {
            repository.removeFavorite(id)
            _toastMessage.value = "Removed from favorites"
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            _toastMessage.value = "History cleared"
        }
    }

    fun deleteHistoryItem(id: String) {
        viewModelScope.launch {
            repository.deleteHistoryItem(id)
        }
    }
}
