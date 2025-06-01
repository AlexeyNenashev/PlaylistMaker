package com.example.playlistmaker.ui.player

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.SearchActivity
import com.google.gson.Gson
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val COUNTER_DELAY = 500L
    }

    private enum class PlayerState {
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED,
        NO_URL;
    }

    private enum class PlayButtonState {
        PLAY,
        PAUSE;
    }

    private var playerState = PlayerState.DEFAULT

    private lateinit var play: ImageButton
    private lateinit var seconds: TextView
    private var url: String? = ""
    private lateinit var playerInteractor: PlayerInteractor

    private var mainThreadHandler: Handler? = null


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

        mainThreadHandler = Handler(Looper.getMainLooper())
        playerInteractor = Creator.providePlayerInteractor()

        val cover = findViewById<ImageView>(R.id.cover)
        val title = findViewById<TextView>(R.id.title)
        val author = findViewById<TextView>(R.id.author)
        val duration = findViewById<TextView>(R.id.duration)
        val album = findViewById<TextView>(R.id.album)
        val albumHeader = findViewById<TextView>(R.id.albumHeader)
        val year = findViewById<TextView>(R.id.year)
        val genre = findViewById<TextView>(R.id.genre)
        val country = findViewById<TextView>(R.id.country)
        play = findViewById(R.id.play_button)
        seconds = findViewById(R.id.time_now)

        val json = intent.getStringExtra(SearchActivity.EXTRA_TRACK)
        if (json != null) {
            val track: Track = Gson().fromJson(json, Track::class.java)
            val radius: Int = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10.0F,
                cover.resources.displayMetrics
            ).toInt()
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

            if (track.previewUrl.isNullOrEmpty()) {
                playerState = PlayerState.NO_URL
            } else {
                url = track.previewUrl
                preparePlayer()
                play.setOnClickListener {
                    playbackControl()
                }
            }

        }

    }


    override fun onPause() {
        super.onPause()
        if (playerState != PlayerState.NO_URL) {
            pausePlayer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.release()
        mainThreadHandler?.removeCallbacks(createUpdateTimerTask())
    }


    private fun playbackControl() {
        when (playerState) {
            PlayerState.PLAYING -> {
                pausePlayer()
            }
            PlayerState.PREPARED, PlayerState.PAUSED -> {
                startPlayer()
            }
            else -> {}
        }
    }

    private fun preparePlayer() {
        url?.let {
            playerInteractor.prepare(it,
                {
                    play.isEnabled = true
                    playerState = PlayerState.PREPARED
                },
                {
                    setPlayButtonState(PlayButtonState.PLAY)
                    playerState = PlayerState.PREPARED
                    mainThreadHandler?.removeCallbacks(createUpdateTimerTask())
                    seconds.text = "00:00"
                }
            )
        }
    }

    private fun startPlayer() {
        playerInteractor.play()
        setPlayButtonState(PlayButtonState.PAUSE)
        playerState = PlayerState.PLAYING
        mainThreadHandler?.post(
            createUpdateTimerTask()
        )
    }

    private fun pausePlayer() {
        playerInteractor.pause()
        setPlayButtonState(PlayButtonState.PLAY)
        playerState = PlayerState.PAUSED
        mainThreadHandler?.removeCallbacks(createUpdateTimerTask())
    }

    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                if (playerState == PlayerState.PLAYING) {
                    seconds.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerInteractor.getCurrentPosition())
                    mainThreadHandler?.postDelayed(this, COUNTER_DELAY)
                }
            }
        }
    }

    private fun setPlayButtonState(state: PlayButtonState) {
        when (state) {
            PlayButtonState.PLAY -> {
                play.setImageResource(R.drawable.button_play)
            }
            PlayButtonState.PAUSE -> {
                play.setImageResource(R.drawable.button_pause)
            }
        }
    }

}
