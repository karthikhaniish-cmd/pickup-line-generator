package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.FavoriteLine
import com.example.data.model.HistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface RizzDao {
    // Favorites
    @Query("SELECT * FROM favorites ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<FavoriteLine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(line: FavoriteLine)

    @Query("DELETE FROM favorites WHERE id = :id")
    suspend fun deleteFavoriteById(id: String)

    @Query("DELETE FROM favorites WHERE text = :text")
    suspend fun deleteFavoriteByText(text: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE text = :text)")
    fun isFavorite(text: String): Flow<Boolean>

    // History
    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<HistoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItems(items: List<HistoryItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItem(item: HistoryItem)

    @Query("DELETE FROM history")
    suspend fun clearHistory()

    @Query("DELETE FROM history WHERE id = :id")
    suspend fun deleteHistoryItem(id: String)
}
