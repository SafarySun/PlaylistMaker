package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View

import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.audioplayer.ui.AudioPlayerActivity
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.presentation.SearchViewModel
import com.practicum.playlistmaker.search.ui.model.TrackState


class SearchActivity : ComponentActivity() {

    private val adapterHistory = TrackAdapter(
        object : ClickListernForTrack {
            override fun onTrackClickListern(track: Track) {
                if (clickDebounce()) {
                    startTrack(track)
                }

            }
        })
    private val adapter = TrackAdapter(
        object : ClickListernForTrack {
            override fun onTrackClickListern(track: Track) {
                if (clickDebounce()) {
                    viewModel.addTrackToHistory(track)
                    startTrack(track)
                    adapterHistory.notifyItemRemoved(0)
                    adapterHistory.notifyItemRangeChanged(0, adapterHistory.tracks.size)
                }

            }
        })

    private lateinit var binding: ActivitySearchBinding
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())


    private val viewModel by lazy {
        ViewModelProvider(this, SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]
    }

    private var textWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observeState().observe(this) {
            render(it)
        }
        viewModel.observeShowToast().observe(this) {
            showToast(it)
        }

        binding.recycleViewSearch.adapter = adapter
        binding.rvHistory.adapter = adapterHistory

        // Кнопка назад
        binding.backButton.setOnClickListener {
            finish()
        }

        // Фокусировка
        binding.inputEditText.setOnClickListener {
            binding.inputEditText.requestFocus()
        }

        //Очистить строку и убрать клаву
        binding.clearIcon.setOnClickListener {
            binding.inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.inputEditText.windowToken, 0)

            binding.placeholderHead.visibility = View.GONE
            binding.buttonUpdate.visibility = View.GONE
            adapter.tracks.clear()
            adapter.notifyDataSetChanged()
            adapterHistory.notifyDataSetChanged()
            binding.historyHead.isVisible = adapterHistory.tracks.isNotEmpty()
        }

        //Наблюдатель Текста

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(changedText = s?.toString() ?: "")
                binding.clearIcon.visibility = clearButtonVisibility(s)
                if (binding.inputEditText.hasFocus() && binding.inputEditText.text.isEmpty()) {
                    showEmpty()
                }

                }


            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        textWatcher?.let { binding.inputEditText.addTextChangedListener(it) }


        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
            adapterHistory.tracks.clear()
            showEmpty()

        }
        binding.buttonUpdate.setOnClickListener {
            viewModel.updateSearch()
        }

        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty() && adapterHistory.tracks.isNotEmpty()) {
                binding.historyHead.isVisible = true
            }

        }

    }


    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { binding.inputEditText.removeTextChangedListener(it) }
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_LONG).show()
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
            is TrackState.Empty -> showEmpty(state.message)
            is TrackState.Error -> showError(state.errorMessage)
            is TrackState.Loading -> showLoading()
            is TrackState.HistoryContent -> showHistory(state.track)
            is TrackState.HistoryEmpty -> showEmpty()
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

    private fun showEmpty() {
        adapterHistory.notifyDataSetChanged()
        binding.recycleViewSearch.visibility = View.GONE
        binding.historyHead.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.rvHistory.visibility = View.GONE
        Log.d("SHOW","EMTY")
    }

    private fun showLoading() {
        binding.recycleViewSearch.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        binding.historyHead.visibility = View.GONE
        binding.placeholderHead.visibility = View.GONE
        binding.buttonUpdate.visibility = View.GONE

    }

    private fun showError(errorMessage: String) {
        binding.recycleViewSearch.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderHead.visibility = View.VISIBLE
        binding.imageError.setImageResource(R.drawable.error_internet)
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.text = errorMessage
        binding.buttonUpdate.visibility = View.VISIBLE


    }

    private fun showEmpty(emptyMessage: String) {
        binding.recycleViewSearch.visibility = View.GONE
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderHead.visibility = View.VISIBLE
        binding.imageError.setImageResource(R.drawable.error_smile_empty)
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.text = emptyMessage
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
        val intent = Intent(this, AudioPlayerActivity::class.java)
        intent.putExtra(TRANSITION, track)
        startActivity(intent)

    }

    companion object {
        const val TRANSITION = "TRANSITION"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

}