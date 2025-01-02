package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter (
    private val data: List<Track>, private val clickable: Boolean
) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
        if (clickable) {
            holder.trackView.setOnClickListener { view ->
                updateTrackHistory(data[position])
                saveSearchHistory()
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun updateTrackHistory(newTrack: Track) {
        searchHistoryItems.removeAll { it.trackId == newTrack.trackId }
        searchHistoryItems.add(0, newTrack)
        while (searchHistoryItems.size > 10) {
            searchHistoryItems.removeLast()
        }
    }
}
