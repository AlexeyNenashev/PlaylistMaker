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
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.SearchActivity
import com.google.gson.Gson
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private var viewModel: PlayerViewModel? = null
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.menuButton.setOnClickListener { finish() }

        var url: String? = ""

        val json = intent.getStringExtra(SearchActivity.EXTRA_TRACK)
        if (json != null) {
            val track: Track = Gson().fromJson(json, Track::class.java)
            val radius: Int = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10.0F,
                binding.cover.resources.displayMetrics
            ).toInt()
            Glide.with(this)
                .load(track.artworkUrlCover)
                .placeholder(R.drawable.placeholder_big)
                .fitCenter()
                .transform(RoundedCorners(radius))
                .into(binding.cover)
            binding.title.text = track.trackName
            binding.author.text = track.artistName
            binding.duration.text = track.trackTime
            if (track.collectionName.isEmpty()) {
                binding.album.visibility = View.GONE
                binding.albumHeader.visibility = View.GONE
            } else {
                binding.album.text = track.collectionName
                binding.album.visibility = View.VISIBLE
                binding.albumHeader.visibility = View.VISIBLE
            }
            binding.year.text = track.year
            binding.genre.text = track.primaryGenreName
            binding.country.text = track.country

            if (track.previewUrl.isNotEmpty()) {

                url = track.previewUrl

                viewModel = ViewModelProvider(this, PlayerViewModel.getFactory(url))
                    .get(PlayerViewModel::class.java)

                viewModel?.observePlayerState()?.observe(this) {
                    setPlayButtonState(it == PlayerViewModel.STATE_PLAYING)
                }

                viewModel?.observeProgressTime()?.observe(this) {
                    binding.timeNow.text = it
                }

                binding.playButton.setOnClickListener {
                    viewModel?.onPlayButtonClicked()
                }

             }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel?.onPause()
    }

    private fun setPlayButtonState(isPlaying: Boolean) {
        if (isPlaying) {
            binding.playButton.setImageResource(R.drawable.button_pause)
        } else {
            binding.playButton.setImageResource(R.drawable.button_play)
        }
    }

}
