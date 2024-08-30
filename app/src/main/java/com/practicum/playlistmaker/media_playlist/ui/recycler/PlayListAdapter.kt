package com.practicum.playlistmaker.media_playlist.ui.recycler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.ItemPlayListBinding
import com.practicum.playlistmaker.media_creation.domain.model.PlayList


class PlayListAdapter(private val playLists: List<PlayList>): RecyclerView.Adapter<PlayListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =ItemPlayListBinding.inflate(layoutInflater, parent, false)
        return PlayListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return playLists.size
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(playLists[position])
    }
}
