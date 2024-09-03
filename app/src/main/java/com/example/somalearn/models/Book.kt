package com.example.somalearn.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book(
    @SerializedName("artistName")
    val artistName: String,

    @SerializedName("artworkUrl100")
    val artworkUrl100: String,

    @SerializedName("averageUserRating")
    val averageUserRating: Double,

    @SerializedName("description")
    val description: String,

    @SerializedName("formattedPrice")
    val formattedPrice: String,

    @SerializedName("trackId")
    val trackId: Int,

    @SerializedName("trackName")
    val trackName: String
) : Parcelable
