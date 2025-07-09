package com.example.playlistmaker.data.search

interface StorageClient<T> {
    fun storeData(data: T)
    fun getData(): T?
}