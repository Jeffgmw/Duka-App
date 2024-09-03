package com.example.somalearn.data.local

import androidx.room.*
import com.example.somalearn.data.local.entities.CachedBook
import com.example.somalearn.utils.Constants
import kotlinx.coroutines.flow.Flow

@Dao
interface BooksDao {
    @Query("SELECT * FROM ${Constants.ROOM.CACHED_BOOKS_TABLE} WHERE status=:status ORDER BY id ASC")
    fun getCachedBooks(
        status: Int
    ): Flow<List<CachedBook>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(
        book: CachedBook
    )

    @Query("DELETE FROM ${Constants.ROOM.CACHED_BOOKS_TABLE} WHERE id=:bookId")
    suspend fun removeBook(
        bookId: Int
    )
}
