package com.example.somalearn.data.local

import androidx.room.TypeConverter
import com.example.somalearn.models.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class BooksTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun bookToJson(book: Book): String {
        return gson.toJson(book)
    }

    @TypeConverter
    fun stringToBook(book: String): Book {
        val dataType = object: TypeToken<Book>() {}.type
        return gson.fromJson(book, dataType)
    }
}
