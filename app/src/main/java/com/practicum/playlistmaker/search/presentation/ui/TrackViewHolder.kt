package com.practicum.playlistmaker.search.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.formatDuration
import com.practicum.playlistmaker.search.domain.models.Track


class TrackViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater
        .from(parent.context)
        .inflate(R.layout.track_frame, parent, false)
) {


    private val ivImageArtist: ImageView = itemView.findViewById(R.id.image_artist)
    private val tvArtistName: TextView = itemView.findViewById(R.id.artist_name)
    private val tvTrackName: TextView = itemView.findViewById(R.id.track_name)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.track_time)


    fun bind(item: Track) {
        Glide.with(itemView)
            .load(item.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(5))
            .placeholder(R.drawable.placeholder)
            .into(ivImageArtist)




        tvArtistName.text = item.artistName
        tvArtistName.requestLayout()
        tvTrackName.text = item.trackName
        tvTrackTime.text = formatDuration(item.trackTimeMillis)


    }


}