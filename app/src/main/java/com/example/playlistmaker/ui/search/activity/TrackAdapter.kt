package com.example.playlistmaker.ui.search.activity

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.model.Track

class TrackAdapter (
    private val clickListener: TrackClickListener,
    private val longClickListener: TrackLongClickListener?
    ) : RecyclerView.Adapter<TrackViewHolder> () {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder.from(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks.get(position))
        holder.itemView.setOnClickListener { clickListener.onTrackClick(tracks[position]) }
        if (longClickListener != null) {
            holder.itemView.setOnLongClickListener { longClickListener.onTrackClick(tracks[position]) }
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun interface TrackClickListener {
        fun onTrackClick(track: Track)
    }

    fun interface TrackLongClickListener {
        fun onTrackClick(track: Track): Boolean
    }

}
