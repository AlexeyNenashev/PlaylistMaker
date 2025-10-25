package com.example.playlistmaker.ui.library.activity

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Playlist

class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val titleView: TextView = itemView.findViewById(R.id.title)
    private val numTracksView: TextView = itemView.findViewById(R.id.numTracks)
    private val pictureView: ImageView = itemView.findViewById(R.id.picture)

    fun bind(playlist: Playlist) {
        Log.d("playlist", playlist.name)
        titleView.text = playlist.name
        numTracksView.text = "${playlist.trackIds.size} треков"
        if (playlist.imageUri.isEmpty()) {
            pictureView.setImageResource(R.drawable.placeholder_big)
        } else {
            pictureView.setImageURI(playlist.imageUri.toUri())
        }
    }

}