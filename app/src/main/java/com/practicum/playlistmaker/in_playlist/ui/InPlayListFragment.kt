package com.practicum.playlistmaker.in_playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentInPlaylistBinding
import com.practicum.playlistmaker.in_playlist.ui.recycler.ClickOnTrack
import com.practicum.playlistmaker.in_playlist.ui.recycler.InPlaylistAdapter
import com.practicum.playlistmaker.in_playlist.view_model.InPlayListViewModel
import com.practicum.playlistmaker.in_playlist.view_model.states.MenuState
import com.practicum.playlistmaker.in_playlist.view_model.states.PLDetailsState
import com.practicum.playlistmaker.in_playlist.view_model.states.TrackDetailsState
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.utils.debounce
import com.practicum.playlistmaker.utils.dpToPx
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class InPlayListFragment : Fragment() {

    private val viewModel: InPlayListViewModel by viewModel {
        parametersOf(playlistId)
    }
    private var playlistId = 0
    private lateinit var binding: FragmentInPlaylistBinding

    private lateinit var deleteTrackDialog: MaterialAlertDialogBuilder

    private val adapter = InPlaylistAdapter(object : ClickOnTrack {

        override fun onTrackClickListern(track: Track) {
            onClickTrackDebounce(track)
        }

        override fun onTrackLongClickListener(track: Track) {
            deleteTrackDialog.show()
            viewModel.onTrackLongClicked(track)

        }
    })
    private lateinit var onClickTrackDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val bottomSheetBehaviorTrack = BottomSheetBehavior.from(binding.tracksBottomSheet)
        bottomSheetBehaviorTrack.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        bottomSheetBehaviorTrack.halfExpandedRatio = 0.3f
        bottomSheetBehaviorTrack.peekHeight = dpToPx(requireContext(), BOTTOM_SHEET)
        val bottomSheetBehaviorMenu = BottomSheetBehavior.from(binding.openMenu).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            viewModel.closeMenu()
                            binding.overlay.visibility = View.GONE

                        }

                        else -> binding.overlay.visibility = View.VISIBLE
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
        }


        deleteTrackDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.view_delete_track))
            .setNegativeButton(getString(R.string.view_no)) { _, _ -> }
            .setPositiveButton(getString(R.string.view_yes)) { _, _ ->
                viewModel.deleteTrack()
            }
        val deletePlaylistDialog =
            MaterialAlertDialogBuilder(requireContext())
                .setMessage(getString(R.string.want_delete_playlist))
                .setNegativeButton(getString(R.string.view_no)) { _, _ -> }
                .setPositiveButton(getString(R.string.view_yes)) { _, _ ->
                    lifecycleScope.launch {
                        viewModel.playlistDeleteConfirmed()
                        findNavController().navigateUp()

                    }
                }

        binding.recyclerViewTracks.adapter = adapter

        onClickTrackDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            startTrack(track)
        }


        val args: InPlayListFragmentArgs by navArgs()
        playlistId = args.playlistId

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
        viewModel.observeDetailsPL().observe(viewLifecycleOwner) { state ->
            renderPlState(state)
        }
        viewModel.observeDetailsTrack().observe(viewLifecycleOwner) { state ->
            renderTrackState(state)
        }
        viewModel.observeShowToast().observe(viewLifecycleOwner) {
            showToast(it)
        }

        viewModel.observeMenuState().observe(viewLifecycleOwner) { state ->
            when (state) {
                MenuState.SHOW -> {
                    bottomSheetBehaviorMenu.state =
                        BottomSheetBehavior.STATE_HALF_EXPANDED
                    binding.tracksBottomSheet.isVisible = false
                }

                MenuState.NONE -> {
                    bottomSheetBehaviorMenu.state =
                        BottomSheetBehavior.STATE_HIDDEN
                    binding.tracksBottomSheet.isVisible = true

                }
            }
        }

        binding.options.setOnClickListener {
            viewModel.openMenu()
        }
        binding.share.setOnClickListener {
            viewModel.onShareClick(binding.amountOfTracks.text.toString())
        }
        binding.shareTrack.setOnClickListener {
            viewModel.onShareClick(binding.amountOfTracks.text.toString())
        }
        binding.deletePlaylist.setOnClickListener{
            deletePlaylistDialog.show()
        }
        binding.edit.setOnClickListener{
            enterToEdit(playlistId)
        }
    }

    private fun renderTrackState(stateTrack: TrackDetailsState) {
        binding.apply {

            durationPlaylist.text =
                resources.getQuantityString(
                    R.plurals.plurals_minutes,
                    stateTrack.durationTracks,
                    stateTrack.durationTracks
                )
            val pluralsTracks = resources.getQuantityString(
                R.plurals.plurals_tracks,
                stateTrack.amountOfTracks,
                stateTrack.amountOfTracks
            )
            amountOfTracks.text = pluralsTracks
            includedLayout.amountTracks.text = pluralsTracks

            if (stateTrack.tracks.isEmpty()) {
                recyclerViewTracks.isVisible = false
                emptyPlaylist.isVisible = true
            } else {
                emptyPlaylist.isVisible = false
                recyclerViewTracks.isVisible = true
            }
        }
        adapter.tracks.clear()
        adapter.tracks.addAll(stateTrack.tracks)
        adapter.notifyDataSetChanged()
    }


    private fun renderPlState(statePl: PLDetailsState) {
        binding.apply {
            setupImage(statePl.image)
            namePlaylist.text = statePl.name
            descriptionTv.text = statePl.description
            includedLayout.playlistName.text = statePl.name
        }
    }

    private fun setupImage(url: String) {
        Glide.with(binding.imageView)
            .load(url)
            .transform(CenterCrop())
            .placeholder(R.drawable.placeholder_big)
            .into(binding.imageView)

        Glide.with(binding.includedLayout.imagePlaylist)
            .load(url)
            .transform(CenterCrop(), RoundedCorners(dpToPx(binding.root.context, 8)))
            .placeholder(R.drawable.placeholder_ap)
            .into(binding.includedLayout.imagePlaylist)
    }

    private fun startTrack(track: Track) {
        val action =
            InPlayListFragmentDirections.actionInPlayListFragmentToAudioPlayerFragment(track)
        findNavController().navigate(action)
    }

    private fun showToast(additionalMessage: String?) {
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_SHORT).show()
    }
    private fun enterToEdit(playlistId:Int){
        val action = InPlayListFragmentDirections.actionInPlayListFragmentToPlayListEditInfoFragment(playlistId)
        findNavController().navigate(action)
    }

    override fun onResume() {
        viewModel.onResume()
        super.onResume()
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val BOTTOM_SHEET = 238
    }

}