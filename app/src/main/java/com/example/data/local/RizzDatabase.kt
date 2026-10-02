package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.FavoriteLine
import com.example.data.model.HistoryItem

@Database(entities = [FavoriteLine::class, HistoryItem::class], version = 1, exportSchema = false)
abstract class RizzDatabase : RoomDatabase() {
    abstract fun rizzDao(): RizzDao

    companion object {
        @Volatile
        private var INSTANCE: RizzDatabase? = null

        fun getInstance(context: Context): RizzDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RizzDatabase::class.java,
                    "rizzai_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
