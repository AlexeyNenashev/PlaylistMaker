package com.example.playlistmaker.data.search

interface StorageClient<T> {
    suspend fun storeData(data: T)
    suspend fun getData(): T?
}