package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

data class PickupLine(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val style: String,
    val tone: String,
    val language: String = "Tanglish",
    val isFavorite: Boolean = false
)

data class GenerationRequest(
    val name: String = "",
    val situation: String = "First time talking",
    val language: String = "Tanglish",
    val style: String = "Funny",
    val confidence: String = "Casual",
    val count: Int = 5
)

@Entity(tableName = "favorites")
data class FavoriteLine(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val style: String,
    val tone: String,
    val language: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "history")
data class HistoryItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val style: String,
    val tone: String,
    val language: String,
    val targetName: String? = null,
    val situation: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
