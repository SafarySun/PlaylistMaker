package com.practicum.playlistmaker.search.ui.model.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TrackFrameBinding
import com.practicum.playlistmaker.search.domain.models.Track

class TrackAdapter(private val clickListern: ClickListernForTrack) :
    RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TrackFrameBinding.inflate(layoutInflater, parent, false)
        return TrackViewHolder(binding)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position],clickListern)
    }
}


fun interface ClickListernForTrack{

   fun onTrackClickListern(track: Track)
}