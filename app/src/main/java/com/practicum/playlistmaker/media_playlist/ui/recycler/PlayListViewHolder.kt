package com.practicum.playlistmaker.media_playlist.ui.recycler

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ItemPlayListBinding
import com.practicum.playlistmaker.media_creation.domain.model.PlayList

class PlayListViewHolder(private val binding: ItemPlayListBinding):
    RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(playlist: PlayList) {
            Glide.with(binding.root)
                .load(playlist.coverImage)
                .centerCrop()
                .transform(CenterCrop(), RoundedCorners(16))
                .placeholder(R.drawable.placeholder_ap)
                .into(binding.playlistImage)

            binding.amountTracks.text =  binding.root.context.getString(R.string.tracks, playlist.amountTracks)
            binding.namePlaylist.text = playlist.name
        }
    }