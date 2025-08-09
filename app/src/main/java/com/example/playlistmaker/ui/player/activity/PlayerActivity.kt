package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.ui.player.PlayerState
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.activity.SearchActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {

    private var json: String? = ""
    private val viewModel by viewModel<PlayerViewModel> {
        parametersOf(json)
    }
    private lateinit var binding: ActivityPlayerBinding
    private var isTextRendered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.menuButton.setOnClickListener { finish() }
        json = intent.getStringExtra(SearchActivity.Companion.EXTRA_TRACK)
        if (json != null) {
            viewModel.observePlayerState().observe(this) {
                render(it)
            }
            binding.playButton.setOnClickListener {
                viewModel.onPlayButtonClicked()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun render(state: PlayerState) {
        setPlayButtonState(state.isPlaying)
        binding.timeNow.text = state.progressTime
        if (!isTextRendered) {
            isTextRendered = true
            val radius: Int = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10.0F,
                binding.cover.resources.displayMetrics
            ).toInt()
            Glide.with(this)
                .load(state.artworkUrlCover)
                .placeholder(R.drawable.placeholder_big)
                .fitCenter()
                .transform(RoundedCorners(radius))
                .into(binding.cover)
            binding.title.text = state.trackName
            binding.author.text = state.artistName
            binding.duration.text = state.trackTime
            if (state.collectionName.isEmpty()) {
                binding.album.visibility = View.GONE
                binding.albumHeader.visibility = View.GONE
            } else {
                binding.album.text = state.collectionName
                binding.album.visibility = View.VISIBLE
                binding.albumHeader.visibility = View.VISIBLE
            }
            binding.year.text = state.year
            binding.genre.text = state.primaryGenreName
            binding.country.text = state.country
        }
    }

    private fun setPlayButtonState(isPlaying: Boolean) {
        if (isPlaying) {
            binding.playButton.setImageResource(R.drawable.button_pause)
        } else {
            binding.playButton.setImageResource(R.drawable.button_play)
        }
    }

}