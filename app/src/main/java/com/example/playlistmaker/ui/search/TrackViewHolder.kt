package com.example.playlistmaker.ui.search

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackItemBinding
import com.example.playlistmaker.domain.search.model.Track

class TrackViewHolder(private val binding: TrackItemBinding): RecyclerView.ViewHolder(binding.root) {

    private val radius: Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        2.0F,
        itemView.resources.displayMetrics).toInt()

    fun bind(item: Track) {
        binding.trackname.text = item.trackName
        binding.author.text = item.artistName
        binding.time.text = item.trackTime
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(radius))
            .into(binding.picture)
    }

    companion object {
        fun from(parent: ViewGroup): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = TrackItemBinding.inflate(inflater, parent, false)
            return TrackViewHolder(binding)
        }
    }

}
