package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var playerState = STATE_DEFAULT

    private lateinit var play: ImageButton
    private var mediaPlayer = MediaPlayer()
    private var url: String? = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview112/v4/ac/c7/d1/acc7d13f-6634-495f-caf6-491eccb505e8/mzaf_4002676889906514534.plus.aac.p.m4a"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_audio_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val menuButton = findViewById<ImageButton>(R.id.menu_button)
        menuButton.setOnClickListener {
            finish()
        }

        val cover = findViewById<ImageView>(R.id.cover)
        val title = findViewById<TextView>(R.id.title)
        val author = findViewById<TextView>(R.id.author)
        val duration = findViewById<TextView>(R.id.duration)
        val album = findViewById<TextView>(R.id.album)
        val albumHeader = findViewById<TextView>(R.id.albumHeader)
        val year = findViewById<TextView>(R.id.year)
        val genre = findViewById<TextView>(R.id.genre)
        val country = findViewById<TextView>(R.id.country)

        val json = intent.getStringExtra(SearchActivity.EXTRA_TRACK)
        if (json != null) {
            val track: Track = Gson().fromJson(json, Track::class.java)
            val radius: Int = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10.0F,
                cover.resources.displayMetrics).toInt()
            Glide.with(this)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.placeholder_big)
                .fitCenter()
                .transform(RoundedCorners(radius))
                .into(cover)
            title.text = track.trackName
            author.text = track.artistName
            duration.text = track.trackTime()
            if ((track.collectionName + "").isEmpty() || track.collectionName + "" == "null") {
                album.visibility = View.GONE
                albumHeader.visibility = View.GONE
            } else {
                album.text = track.collectionName
                album.visibility = View.VISIBLE
                albumHeader.visibility = View.VISIBLE
            }
            year.text = track.getYear()
            genre.text = track.primaryGenreName
            country.text = track.country
        }

        play = findViewById(R.id.play_button)

        preparePlayer()

        play.setOnClickListener {
            playbackControl()
        }

    }


    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }




    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            //play.text = "PLAY"
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        //play.text = "PAUSE"
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        //play.text = "PLAY"
        playerState = STATE_PAUSED
    }



}