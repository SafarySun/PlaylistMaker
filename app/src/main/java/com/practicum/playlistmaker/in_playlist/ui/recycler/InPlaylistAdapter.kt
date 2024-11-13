package com.practicum.playlistmaker.in_playlist.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.TrackFrameBinding
import com.practicum.playlistmaker.search.domain.models.Track

class InPlaylistAdapter(private val clickListern: ClickOnTrack) :
    RecyclerView.Adapter<InPlayListViewHolder>() {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InPlayListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TrackFrameBinding.inflate(layoutInflater, parent, false)
        return InPlayListViewHolder(binding)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: InPlayListViewHolder, position: Int) {
        holder.bind(tracks[position],clickListern)
    }
}


 interface ClickOnTrack{
    fun onTrackClickListern(track: Track)
    fun onTrackLongClickListener(track: Track)
}