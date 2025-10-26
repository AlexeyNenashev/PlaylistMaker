package com.example.playlistmaker.ui.library.activity

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
        titleView.text = playlist.name
        numTracksView.text = trackNumberString(playlist.trackIds.size)
        if (playlist.imageUri.isEmpty()) {
            pictureView.setImageResource(R.drawable.placeholder_big)
        } else {
            pictureView.setImageURI(playlist.imageUri.toUri())
        }
    }

    private fun trackNumberString(trackNumber: Int): String {
        val n10  = trackNumber % 10
        val n100 = trackNumber % 100
        var s = "$trackNumber треков"
        if (n10 == 1 && n100 != 11) { s = "$trackNumber трек" }
        if (n10 == 2 && n100 != 12) { s = "$trackNumber трека" }
        if (n10 == 3 && n100 != 13) { s = "$trackNumber трека" }
        if (n10 == 4 && n100 != 14) { s = "$trackNumber трека" }
        return s
    }

}