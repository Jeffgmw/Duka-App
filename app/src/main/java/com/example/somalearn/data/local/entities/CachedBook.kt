package com.example.somalearn.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.somalearn.models.Book
import com.example.somalearn.utils.Constants

@Entity(tableName = Constants.ROOM.CACHED_BOOKS_TABLE)
data class CachedBook(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val status: Int,
    val book: Book
)
