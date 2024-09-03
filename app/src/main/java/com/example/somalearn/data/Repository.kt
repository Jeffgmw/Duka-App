package com.example.somalearn.data

import javax.inject.Inject

class Repository @Inject constructor(
    networkDataSource: NetworkDataSource,
    localDataSource: LocalDataSource
) {
    val remote = networkDataSource
    val local = localDataSource
}
