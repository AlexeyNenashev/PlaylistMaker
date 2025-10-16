package com.example.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.search.NetworkClient
import com.example.playlistmaker.data.search.StorageClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.storage.PrefsStorageClient
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.settings.model.ThemeSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<StorageClient<ArrayList<Track>>>(named("historyStorage")) {
        PrefsStorageClient<ArrayList<Track>>(get(), get(), "history",
            object : TypeToken<ArrayList<Track>>() {}.type
        )
    }

    single<StorageClient<ThemeSettings>>(named("themeStorage")) {
        PrefsStorageClient<ThemeSettings>(get(), get(),"dark_theme",
            object : TypeToken<ThemeSettings>() {}.type
        )
    }

    single {
        androidContext().getSharedPreferences("preferences", Context.MODE_PRIVATE)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    factory { Gson() }

    factory{ MediaPlayer() }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build().trackDao()
    }

}