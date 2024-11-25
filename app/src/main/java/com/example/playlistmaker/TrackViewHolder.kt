package com.example.playlistmaker

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val picture: ImageView = itemView.findViewById(R.id.picture)
    private val trackName: TextView = itemView.findViewById(R.id.trackname)
    private val author: TextView = itemView.findViewById(R.id.author)
    private val time: TextView = itemView.findViewById(R.id.time)
    private val radius: Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        2.0F,
        itemView.resources.displayMetrics).toInt()

    fun bind(item: Track) {
        trackName.text = item.trackName
        author.text = item.artistName
        time.text = item.trackTime
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .fitCenter()
            .transform(RoundedCorners(radius))
            .into(picture)
    }
}
