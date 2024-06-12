package com.practicum.playlistmaker.media_favorite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FavoriteFragmentBinding
import com.practicum.playlistmaker.media_favorite.view_model.FavoriteContentState
import com.practicum.playlistmaker.media_favorite.view_model.FavoriteFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment: Fragment() {

    companion object {
        fun newInstance() = FavoritesFragment()
            }

    private val viewModel: FavoriteFragmentViewModel by viewModel()


    private lateinit var binding: FavoriteFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getContentState().observe(viewLifecycleOwner) {
            when(it){
                FavoriteContentState.EMPTY -> showError()
                FavoriteContentState.CONTENT -> showError()
            }
        }

            // binding.textAboutError.text = getString(R.string.)
            //requireArguments().getInt(NUMBER).toString()
    }
    fun showError() {
        binding.icon.isVisible = true
        binding.textAboutError.isVisible = true
    }

}