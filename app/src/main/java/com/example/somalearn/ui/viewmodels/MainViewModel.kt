package com.example.somalearn.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.somalearn.data.Repository
import com.example.somalearn.data.local.entities.CachedBook
import com.example.somalearn.models.Book
import com.example.somalearn.models.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    repository: Repository
): AndroidViewModel(application) {

    // REMOTE DATASOURCE

    private val remote = repository.remote

    private val _books: MutableLiveData<NetworkResult<List<Book>>> = MutableLiveData()
    val books: LiveData<NetworkResult<List<Book>>> = _books

    fun listBooks(term: String) {
        _books.value = NetworkResult.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            val booksResponse = remote.searchForBooks(term)
            _books.postValue(booksResponse)
        }
    }


    // LOCAL DATASOURCE

    private val local = repository.local


    val reading = local.getReading().asLiveData()
    val completed = local.getCompleted().asLiveData()

    // used in details fragment
    private val _latestBookRemoved: MutableLiveData<Int?> = MutableLiveData()
    val latestBookRemoved: LiveData<Int?> = _latestBookRemoved

    fun insertBook(book: CachedBook) {
        _latestBookRemoved.value = null
        viewModelScope.launch(Dispatchers.IO) {
            local.insertBook(book)
        }
    }

    fun removeBook(bookId: Int) {
        _latestBookRemoved.value = bookId
        viewModelScope.launch(Dispatchers.IO) {
            local.removeBook(bookId)
        }
    }
}
