package com.example.somalearn.data.network

import com.example.somalearn.models.BooksResponse
import com.example.somalearn.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {
    @GET("search/")
    suspend fun searchForBooks(
        @Query("term") term: String,
        @Query("country") country: String = Constants.API.COUNTRY,
        @Query("media") media: String = Constants.API.MEDIA,
        @Query("limit") limit: Int = Constants.API.LIMIT,
        @Query("explicit") explicit: String = Constants.API.EXPLICIT
    ): Response<BooksResponse>
}
