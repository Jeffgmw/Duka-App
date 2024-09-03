package com.example.somalearn.data

import com.example.somalearn.data.local.BooksDao
import com.example.somalearn.data.local.entities.CachedBook
import com.example.somalearn.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: BooksDao
) {
    fun getReading(): Flow<List<CachedBook>> {
        return recipesDao.getCachedBooks(Constants.STATUS.READING)
    }

    fun getCompleted(): Flow<List<CachedBook>> {
        return recipesDao.getCachedBooks(Constants.STATUS.COMPLETED)
    }

    suspend fun insertBook(book: CachedBook) {
        recipesDao.insertBook(book)
    }

    suspend fun removeBook(bookId: Int) {
        recipesDao.removeBook(bookId)
    }
}
