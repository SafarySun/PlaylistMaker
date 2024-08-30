package com.practicum.playlistmaker.audioplayer.ui.recycler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.databinding.PlaylistFrameBinding
import com.practicum.playlistmaker.media_creation.domain.model.PlayList


class AudioPlayListAdapter(private val clickListern: ClickListernForPlayList):
    RecyclerView.Adapter<AudioPlayListViewHolder>() {

     val playLists = ArrayList<PlayList>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioPlayListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PlaylistFrameBinding.inflate(layoutInflater, parent, false)
        return AudioPlayListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return playLists.size
    }

    override fun onBindViewHolder(holder: AudioPlayListViewHolder, position: Int) {
        holder.bind(playLists[position],clickListern)
    }


}
fun interface ClickListernForPlayList{

    fun onPlayListClickListern(playlist: PlayList)
}
