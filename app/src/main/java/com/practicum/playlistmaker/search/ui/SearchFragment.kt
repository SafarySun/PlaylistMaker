package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.model.TrackState
import com.practicum.playlistmaker.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private val adapterHistory = TrackAdapter(object : ClickListernForTrack {
        override fun onTrackClickListern(track: Track) {
            if (clickDebounce()) {
                startTrack(track)
            }

        }
    })
    private val adapter = TrackAdapter(object : ClickListernForTrack {
        override fun onTrackClickListern(track: Track) {
            if (clickDebounce()) {
                viewModel.addTrackToHistory(track)
                startTrack(track)
                Log.d("error","this point")
                adapterHistory.notifyItemRemoved(0)
                adapterHistory.notifyItemRangeChanged(0, adapterHistory.tracks.size)
            }

        }
    })

    private lateinit var binding: FragmentSearchBinding

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private var textWatcher: TextWatcher? = null

    private val viewModel by viewModel<SearchViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        viewModel.observeShowToast().observe(viewLifecycleOwner) {
            showToast(it)
        }

        binding.recycleViewSearch.adapter = adapter
        binding.rvHistory.adapter = adapterHistory


        // Фокусировка
        binding.inputEditText.setOnClickListener {
            binding.inputEditText.requestFocus()
        }

        //Очистить строку и убрать клаву
        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)

            binding.placeholderHead.visibility = View.GONE
            binding.buttonUpdate.visibility = View.GONE
            adapter.tracks.clear()
            adapter.notifyDataSetChanged()
            adapterHistory.notifyDataSetChanged()

            viewModel.onClearTextClick(
                show = { showHistory(viewModel.getHistory()) },
                empty = { showEmptyScreen() })
        }

        //Наблюдатель Текста

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(changedText = s?.toString() ?: "")

                binding.clearIcon.visibility = clearButtonVisibility(s)
                if (s?.toString().isNullOrEmpty()) {
                    viewModel.onClearTextClick(
                        show = { showHistory(viewModel.getHistory()) },
                        empty = { showEmptyScreen() })
                }

            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        textWatcher?.let { binding.inputEditText.addTextChangedListener(it) }

// ochistit history
        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            adapterHistory.tracks.clear()
            viewModel.getHistory()
            adapterHistory.notifyDataSetChanged()
            //showEmptyScreen()

        }
        binding.buttonUpdate.setOnClickListener {
            viewModel.updateSearch()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher?.let { binding.inputEditText.removeTextChangedListener(it) }
    }

    private fun showToast(additionalMessage: String?) {
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_LONG).show()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun render(state: TrackState) {
        when (state) {
            is TrackState.Content -> showContent(state.track)
            is TrackState.Empty -> showEmptySearch()
            is TrackState.Error -> showError()
            is TrackState.Loading -> showLoading()
            is TrackState.HistoryContent -> showHistory(state.track)
            is TrackState.HistoryEmpty -> showEmptyScreen()
        }
    }

    private fun showHistory(track: List<Track>) {
        adapterHistory.tracks.clear()
        adapterHistory.tracks.addAll(track)
        adapterHistory.notifyDataSetChanged()
        binding.recycleViewSearch.visibility = View.GONE
        binding.historyHead.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.rvHistory.visibility = View.VISIBLE
    }

    private fun showEmptyScreen() {
        adapterHistory.notifyDataSetChanged()
        binding.recycleViewSearch.visibility = View.GONE
        binding.historyHead.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.rvHistory.visibility = View.GONE
    }

    private fun showLoading() {
        binding.recycleViewSearch.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.historyHead.visibility = View.GONE
        binding.placeholderHead.visibility = View.GONE
        binding.buttonUpdate.visibility = View.GONE

    }

    private fun showError() {
        binding.recycleViewSearch.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderHead.visibility = View.VISIBLE
        binding.imageError.setImageResource(R.drawable.error_internet)
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.text = getString(R.string.error_internet)
        binding.buttonUpdate.visibility = View.VISIBLE


    }

    private fun showEmptySearch() {
        binding.recycleViewSearch.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderHead.visibility = View.VISIBLE
        binding.imageError.setImageResource(R.drawable.error_smile_empty)
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.text = getString(R.string.error_empty_search)
        binding.buttonUpdate.visibility = View.VISIBLE

    }

    private fun showContent(track: List<Track>) {
        binding.recycleViewSearch.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        adapter.tracks.clear()
        adapter.tracks.addAll(track)
        adapter.notifyDataSetChanged()

    }

    //Функция - Видимость опз
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun startTrack(track: Track) {
        val action = SearchFragmentDirections.actionSearchFragmentToAudioPlayerActivity(track)
        findNavController().navigate(action)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}