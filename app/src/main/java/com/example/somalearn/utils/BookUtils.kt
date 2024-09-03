package com.example.somalearn.utils

object BookUtils {

    const val COVER_BORDER_RADIUS = 10F

    fun getImageURL(url: String): String {
        return url.replace("100x100", "400x400")
    }
}
