package com.example.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    //private val rootLayout: LinearLayout = itemView.findViewById(R.id.rootLayout)
    private val picture: ImageView = itemView.findViewById(R.id.picture)
    private val trackName: TextView = itemView.findViewById(R.id.trackname)
    private val author: TextView = itemView.findViewById(R.id.author)

    fun bind(item: Track) {
        //picture.setImageResource(R.drawable.placeholder)
        trackName.text = item.trackName
        author.text = item.artistName
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerInside()
            .into(picture)
    }
}
