package com.example.somalearn.models


import com.example.somalearn.models.Book
import com.google.gson.annotations.SerializedName

data class BooksResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val results: List<Book>
)
