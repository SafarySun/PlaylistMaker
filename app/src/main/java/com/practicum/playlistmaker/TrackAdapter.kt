package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter: RecyclerView.Adapter<TrackViewHolder>( ) {
     var tracks = ArrayList<Track>()
     var clickListern:ClickListernForTrack? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
       return TrackViewHolder(parent)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener{
            clickListern?.onTrackClickListern(tracks[position])
           // clickListern?.onTrackClickListern(tracks[holder.adapterPosition])
        }


    }
}
interface ClickListernForTrack{

   fun onTrackClickListern(track:Track)
}