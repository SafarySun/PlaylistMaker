package com.practicum.playlistmaker.media_favorite.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.databinding.FragmentFavoriteBinding
import com.practicum.playlistmaker.media.MediaLibraryFragmentDirections
import com.practicum.playlistmaker.media_favorite.view_model.FavoriteContentState
import com.practicum.playlistmaker.media_favorite.view_model.FavoriteViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.ClickListernForTrack
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val adapterFavorite = TrackAdapter(object : ClickListernForTrack {
        override fun onTrackClickListern(track: Track) {
            onClickTrackDebounce(track)

        }
    })

    private lateinit var onClickTrackDebounce: (Track) -> Unit

    private val viewModel: FavoriteViewModel by viewModel()

    private lateinit var binding: FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getContentState().observe(viewLifecycleOwner) {
            render(it)
        }
        binding.rvFavorit.adapter = adapterFavorite

        onClickTrackDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            startTrack(track)
            adapterFavorite.notifyItemRemoved(0)
            adapterFavorite.notifyItemRangeChanged(0, adapterFavorite.tracks.size)
        }


    }


    private fun render(state: FavoriteContentState) {
        when (state) {
            is FavoriteContentState.ContentState -> showContent(state.track)
            FavoriteContentState.Empty -> showEmpty()
        }
    }

    private fun showEmpty() {
       binding.emptyFavorite.isVisible = true
        binding.rvFavorit.isVisible = false
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showContent(track: List<Track>) {
        adapterFavorite.tracks.clear()
        adapterFavorite.tracks.addAll(track)
        adapterFavorite.notifyDataSetChanged()
        binding.rvFavorit.isVisible = true
        binding.emptyFavorite.isVisible = false


    }

    override fun onResume() {
        super.onResume()
        viewModel.fillData()
    }

    companion object {
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
    private fun startTrack(track: Track) {
        val action = MediaLibraryFragmentDirections.actionMediaLibraryFragmentToAudioPlayerFragment(track)
        findNavController().navigate(action)
    }

}