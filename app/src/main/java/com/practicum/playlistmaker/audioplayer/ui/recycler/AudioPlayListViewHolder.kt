package com.practicum.playlistmaker.audioplayer.ui.recycler

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistFrameBinding
import com.practicum.playlistmaker.media_creation.domain.model.PlayList


class AudioPlayListViewHolder(private val binding: PlaylistFrameBinding):
    RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: PlayList, clickListern:ClickListernForPlayList) {
            Glide.with(binding.root)
                .load(playlist.coverImage)
                .transform(CenterCrop(), RoundedCorners(8))
                .placeholder(R.drawable.placeholder_ap)
                .into(binding.imagePlaylist)

            binding.root.setOnClickListener { clickListern.onPlayListClickListern(playlist) }

            binding.amountTracks.text = binding.root.context.getString(R.string.tracks, playlist.amountTracks)
            binding.playlistName.text = playlist.name
        }
    }



