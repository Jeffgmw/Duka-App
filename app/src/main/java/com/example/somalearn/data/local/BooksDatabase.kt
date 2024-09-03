package com.example.somalearn.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.somalearn.data.local.entities.CachedBook

@Database(
    entities = [CachedBook::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(BooksTypeConverter::class)
abstract class BooksDatabase: RoomDatabase() {
    abstract fun booksDao(): BooksDao
}
