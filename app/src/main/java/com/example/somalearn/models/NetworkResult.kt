package com.example.somalearn.models

sealed class NetworkResult<T>(
    val data: T? = null,
    val error: String? = null
) {
    class Success<T>(data: T): NetworkResult<T>(data, null)
    class Failure<T>(error: String?, data: T? = null): NetworkResult<T>(data, error)
    class Loading<T>: NetworkResult<T>()
}
