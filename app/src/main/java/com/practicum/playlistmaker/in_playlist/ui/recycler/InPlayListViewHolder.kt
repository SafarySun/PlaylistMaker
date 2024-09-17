package com.practicum.playlistmaker.in_playlist.ui.recycler

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackFrameBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.dpToPx
import com.practicum.playlistmaker.utils.formatDuration


class InPlayListViewHolder(private val binding: TrackFrameBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Track, clickListern: ClickOnTrack) {
            Glide.with(binding.root)
                .load(item.artworkUrl100)
                .transform(CenterCrop(), RoundedCorners(dpToPx(binding.root.context,5)))
                .placeholder(R.drawable.placeholder)
                .into(binding.imageArtist)

            binding.root.setOnClickListener {
                clickListern.onTrackClickListern(item)
            }

            binding.root.setOnLongClickListener {
                clickListern.onTrackLongClickListener(item)
                true }

            binding.artistName.text = item.artistName
            binding.artistName.requestLayout()
            binding.trackName.text = item.trackName
            binding.trackTime.text = formatDuration(item.trackTimeMillis)
        }
    }
