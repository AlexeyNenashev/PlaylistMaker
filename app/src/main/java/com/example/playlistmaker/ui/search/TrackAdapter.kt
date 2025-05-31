package com.example.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.models.Track

class TrackAdapter (
    private val data: List<Track>, private val clickable: Boolean, private val history: ArrayList<Track>, private val historyInteractor: HistoryInteractor
) : RecyclerView.Adapter<TrackViewHolder> () {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(data[position])
        holder.trackView.setOnClickListener { view ->
                SearchActivity.processClickOnSearchResult(data[position], view, clickable, history, historyInteractor)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}
