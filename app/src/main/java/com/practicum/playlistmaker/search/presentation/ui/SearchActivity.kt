package com.practicum.playlistmaker.search.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.audioplayer.presentation.ui.AudioPlayerActivity
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.api.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track

class SearchActivity : AppCompatActivity() {

    private val creator = Creator
    private val trackImpl = creator.provideTrackInteractor()

    private lateinit var binding: ActivitySearchBinding
    private var searchText = "VALUE_DEF"
    private val adapter = TrackAdapter()
    private val adapterHistory = TrackAdapter()
    private lateinit var sharedPref: SharedPreferences
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunable = Runnable { searchInApi() }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = getSharedPreferences(SHARED_PREF_ITEM, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPref)
        adapterHistory.tracks = searchHistory.loadTracks()
        // adapterы
        binding.recycleViewSearch.adapter = adapter
        binding.rvHistory.adapter = adapterHistory
        binding.historyHead.isVisible = false
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
        binding.inputEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                searchText = s.toString()
                binding.clearIcon.visibility = clearButtonVisibility(s)
                if (binding.inputEditText.hasFocus() && s?.isEmpty() == true && adapterHistory.tracks.isNotEmpty()) {
                    binding.historyHead.isVisible = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        })
        binding.inputEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty() && adapterHistory.tracks.isNotEmpty()) {
                binding.historyHead.isVisible = true
            }
        }

        // adapter search track
        adapter.clickListern = object : ClickListernForTrack {
            override fun onTrackClickListern(track: Track) {
                if (clickDebounce()) {
                    searchHistory.addTrackToHistory(track)
                    adapterHistory.tracks = searchHistory.loadTracks()
                    adapterHistory.notifyItemRemoved(0)
                    adapterHistory.notifyItemRangeChanged(0, adapterHistory.tracks.size)
                    transition(track)
                }
            }
        }
        //adapter history track
        adapterHistory.clickListern = object : ClickListernForTrack {
            override fun onTrackClickListern(track: Track) {
                if (clickDebounce()) {
                    transition(track)
                }
            }
        }
        // Clear History Button
        binding.clearHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            adapterHistory.tracks.clear()
            adapterHistory.notifyDataSetChanged()
            binding.historyHead.isVisible = adapterHistory.tracks.isNotEmpty()
        }

        //Вешаем на строку ввода слушателя событий
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchInApi()
                // кнопка обновить
                binding.buttonUpdate.setOnClickListener {
                    searchInApi()
                }
            }
            true
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunable)
        handler.postDelayed(searchRunable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchInApi() {
        val querySearch = binding.inputEditText.text.toString().trim()

        trackImpl.searchTracks(querySearch,object : TrackInteractor.TrackConsumer {
            override fun consume(foundTracks: List<Track>) {
                runOnUiThread {
                    if (foundTracks.isNotEmpty()) {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.historyHead.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE

                        adapter.tracks.clear()
                        adapter.tracks.addAll(foundTracks)
                        adapter.notifyDataSetChanged()
                        binding.placeholderHead.visibility = View.GONE

                        if (adapter.tracks.isEmpty()) {
                            adapter.tracks.clear()
                            with(binding) {
                                placeholderHead.visibility = View.VISIBLE
                                placeholderMessage.text = getString(R.string.error_empty_search)
                                imageError.setImageResource(R.drawable.error_smile_empty)
                            }
                            adapter.notifyDataSetChanged()
                        }
                    } else {
                        adapter.tracks.clear()
                        with(binding) {
                            placeholderHead.visibility = View.VISIBLE
                            buttonUpdate.visibility = View.VISIBLE
                            placeholderMessage.text = getString(R.string.error_internet)
                            imageError.setImageResource(R.drawable.error_internet)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }

         /* @SuppressLint("NotifyDataSetChanged")
            override fun onFailure(t: Throwable) {
                adapter.tracks.clear()
                with(binding) {
                    progressBar.visibility = View.GONE
                    placeholderHead.visibility = View.VISIBLE
                    buttonUpdate.visibility = View.VISIBLE
                    placeholderMessage.text = getString(R.string.error_internet)
                    imageError.setImageResource(R.drawable.error_internet)
                }
                adapter.notifyDataSetChanged()
                Toast.makeText(applicationContext, "Нет интернета", Toast.LENGTH_SHORT)
                    .show()
                Log.e("NetworkError", "Error during network request: ${t.message}")
            }*/
        })
    }


private fun clickDebounce(): Boolean {
    val current = isClickAllowed
    if (isClickAllowed) {
        isClickAllowed = false
        handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
    }
    return current
}

override fun onSaveInstanceState(outState: Bundle) {
    outState.putString(QUERY_VALUE, searchText)
    super.onSaveInstanceState(outState)
}

override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    searchText = savedInstanceState.getString(QUERY_VALUE, "")
}

//Функция - Видимость опз
private fun clearButtonVisibility(s: CharSequence?): Int {
    return if (s.isNullOrEmpty()) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

private fun transition(track: Track) {
    val intent = Intent(this, AudioPlayerActivity::class.java)
    intent.putExtra(TRANSITION, track)
    startActivity(intent)

}


companion object {
    private const val QUERY_VALUE = "QUERY_VALUE"
    private const val SHARED_PREF_ITEM = "SHARED_PREF_KEY"
    const val TRANSITION = "TRANSITION"
    private const val CLICK_DEBOUNCE_DELAY = 1000L
    private const val SEARCH_DEBOUNCE_DELAY = 2000L
}
}