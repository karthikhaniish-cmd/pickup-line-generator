package com.example.data.repository

import com.example.data.local.RizzDao
import com.example.data.model.FavoriteLine
import com.example.data.model.GenerationRequest
import com.example.data.model.HistoryItem
import com.example.data.model.PickupLine
import com.example.data.remote.GeminiRizzService
import kotlinx.coroutines.flow.Flow

class RizzRepository(
    private val rizzDao: RizzDao,
    private val geminiService: GeminiRizzService = GeminiRizzService()
) {

    val allFavorites: Flow<List<FavoriteLine>> = rizzDao.getAllFavorites()
    val allHistory: Flow<List<HistoryItem>> = rizzDao.getAllHistory()

    fun isFavorite(text: String): Flow<Boolean> = rizzDao.isFavorite(text)

    suspend fun generateLines(request: GenerationRequest): GeminiRizzService.GenerationResult {
        val result = geminiService.generatePickupLines(request)
        if (result.lines.isNotEmpty()) {
            val historyItems = result.lines.map { line ->
                HistoryItem(
                    text = line.text,
                    style = line.style,
                    tone = line.tone,
                    language = line.language,
                    targetName = request.name.ifBlank { null },
                    situation = request.situation
                )
            }
            rizzDao.insertHistoryItems(historyItems)
        }
        return result
    }

    suspend fun toggleFavorite(line: PickupLine, isCurrentlyFavorite: Boolean) {
        if (isCurrentlyFavorite) {
            rizzDao.deleteFavoriteByText(line.text)
        } else {
            rizzDao.insertFavorite(
                FavoriteLine(
                    id = line.id,
                    text = line.text,
                    style = line.style,
                    tone = line.tone,
                    language = line.language
                )
            )
        }
    }

    suspend fun removeFavorite(id: String) {
        rizzDao.deleteFavoriteById(id)
    }

    suspend fun removeFavoriteByText(text: String) {
        rizzDao.deleteFavoriteByText(text)
    }

    suspend fun clearHistory() {
        rizzDao.clearHistory()
    }

    suspend fun deleteHistoryItem(id: String) {
        rizzDao.deleteHistoryItem(id)
    }
}
