package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val tracks = ArrayList<Track>()
    private var searchValue = ""

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ItunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)

        val trackAdapter = TrackAdapter(tracks)

        val rvTrack = findViewById<RecyclerView>(R.id.rvTrack)
        rvTrack.adapter = trackAdapter

        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
        }

        inputEditText.doOnTextChanged { s, start, before, count ->
            clearButton.visibility = clearButtonVisibility(s)
            searchValue = s.toString()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchValue.isNotEmpty()) {
                    iTunesService.search(searchValue).enqueue(object :
                        Callback<TrackResponse> {
                        override fun onResponse(call: Call<TrackResponse>,
                                                response: Response<TrackResponse>
                        ) {
                            if (response.code() == 200) {
                                tracks.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    Log.d("TestLogTag", "$searchValue: something found")
                                    tracks.addAll(response.body()?.results ?: emptyList<Track>())
                                    Log.d("TestLogTag", "$searchValue: ${tracks.size} found")
                                }
                                trackAdapter.notifyDataSetChanged()
                                if (tracks.isEmpty()) {
                                    //showMessage(getString(R.string.nothing_found), "")
                                    Log.d("TestLogTag", "$searchValue: nothing found")
                                } else {
                                    //showMessage("", "")
                                    Log.d("TestLogTag", "$searchValue: ok")
                                }
                            } else {
                                //showMessage(getString(R.string.something_went_wrong), response.code().toString())
                                Log.d("TestLogTag", "$searchValue: something went wrong, ${response.code()}")
                            }
                        }

                        override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                            //showMessage(getString(R.string.something_went_wrong), t.message.toString())
                            Log.d("TestLogTag", "$searchValue: something went wrong, ${t.message}")
                        }

                    })
                }
                Log.d("TestLogTag", searchValue)
                //true
            }
            //false
            actionId == EditorInfo.IME_ACTION_DONE
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_VALUE, searchValue)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Вторым параметром мы передаём значение по умолчанию
        searchValue = savedInstanceState.getString(SEARCH_VALUE, "")
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        inputEditText.setText(searchValue)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    companion object {
        const val SEARCH_VALUE = "SEARCH_VALUE"
    }

}
