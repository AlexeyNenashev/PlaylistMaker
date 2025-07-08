package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.models.Track

class TrackAdapter (private val clickListener: TrackClickListener) :
    RecyclerView.Adapter<TrackViewHolder> () {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks.get(position))
        holder.trackView.setOnClickListener { clickListener.onTrackClick(tracks.get(position)) }
    }

    override fun getItemCount(): Int = tracks.size

    fun interface TrackClickListener {
        fun onTrackClick(movie: Track)
    }

}
