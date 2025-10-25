package com.example.playlistmaker.ui.library.activity

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Playlist

class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val title: TextView = itemView.findViewById(R.id.title)
    private val description: TextView = itemView.findViewById(R.id.description)

    fun bind(playlist: Playlist) {
        title.text = playlist.name
        description.text = playlist.description
    }

}