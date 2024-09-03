package com.example.somalearn.data

import com.example.somalearn.data.network.ItunesService
import com.example.somalearn.models.Book
import com.example.somalearn.models.NetworkResult
import javax.inject.Inject

class NetworkDataSource @Inject constructor (
    private val itunesService: ItunesService
) {
    suspend fun searchForBooks(term: String): NetworkResult<List<Book>> {
        try {
            val response = itunesService.searchForBooks(term)

            return when {
                response.body()!!.results.isNullOrEmpty() -> {
                    NetworkResult.Failure("Not found")
                }
                response.isSuccessful -> {
                    val booksList = response.body()!!.results
                    NetworkResult.Success(booksList)
                }
                else -> NetworkResult.Failure(response.message())
            }
        }
        catch (exception: Exception) {
            return NetworkResult.Failure(exception.message)
        }
    }
}
