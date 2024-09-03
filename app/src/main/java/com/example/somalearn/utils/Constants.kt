package com.example.somalearn.utils

object Constants {

    object API {
        const val BASE_URL = "https://itunes.apple.com/"

        const val MEDIA = "ebook"
        const val COUNTRY = "us"
        const val LIMIT = 15
        const val EXPLICIT = "no"
    }

    object STATUS {
        const val NONE = 0
        const val READING = 1
        const val COMPLETED = 2
    }

    object ROOM {
        const val DATABASE_NAME = "books_database"
        const val CACHED_BOOKS_TABLE = "cached_books"
    }
}
