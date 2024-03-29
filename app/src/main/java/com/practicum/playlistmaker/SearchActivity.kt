package com.practicum.playlistmaker


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher

import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private var searchText = "VALUE_DEF"
    private val trackBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(trackBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val searchService = retrofit.create(PlayListApi::class.java)
    private val adapter = TrackAdapter()
    private val adapterHistory = TrackAdapter()
    private var imagePH: ImageView? = null
    private var placeholderHead: LinearLayout? = null
    private lateinit var historySearchHead: LinearLayout
    private var placeholderMessage: TextView? = null
    private var updateButton: Button? = null

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val clearHistoryButton = findViewById<Button>(R.id.clear_history_button)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val back = findViewById<ImageView>(R.id.backButton)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        placeholderHead = findViewById(R.id.placeholder_head)
        imagePH = findViewById(R.id.image_error)
        updateButton = findViewById(R.id.button_update)
        sharedPref = getSharedPreferences(SHARED_PREF_ITEM, MODE_PRIVATE)
        historySearchHead = findViewById(R.id.history_head)
        val searchHistory = SearchHistory(sharedPref)
        val trackListFromSharedPerf = searchHistory.loadTracks()
        val rvSearch = findViewById<RecyclerView>(R.id.recycleViewSearch)
        val rvHistori = findViewById<RecyclerView>(R.id.rvHistory)
        searchHistory.loadTracks()
        // Проверка строки
        /*if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(QUERY_VALUE, searchText)
            inputEditText.setText(searchText)
        }*/
        // Кнопка назад
        back.setOnClickListener {
            finish()
        }


        // Фокусировка
        inputEditText.setOnClickListener {
            inputEditText.requestFocus()
        }

            historySearchHead.isVisible = trackListFromSharedPerf.isNotEmpty()



        //Очистить строку и убрать клаву
        clearButton.setOnClickListener {
            inputEditText.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            placeholderHead?.visibility = View.GONE
            updateButton?.visibility = View.GONE
            adapter.tracks.clear()
            adapter.notifyDataSetChanged()




        }
        //Наблюдатель Текста
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                clearButton.visibility = clearButtonVisibility(s)

                historySearchHead.isVisible =
                    inputEditText.hasFocus() && s?.isEmpty() == true && trackListFromSharedPerf.isNotEmpty()


            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnFocusChangeListener { _, hasFocus ->

                historySearchHead.isVisible =
                     (hasFocus && inputEditText.text.isEmpty() && trackListFromSharedPerf.isNotEmpty())



        }
        // adapter search track
        rvSearch.adapter = adapter
        rvHistori.adapter = adapterHistory
        adapterHistory.tracks = trackListFromSharedPerf
        val onClickTrackItem = object : ClickListernForTrack {
            override fun onTrackClickListern(track: Track) {
                searchHistory.addTrackToHistory(track)
                adapterHistory.notifyDataSetChanged()

            }
        }
        adapter.clickListern = onClickTrackItem
        //adapter history track


        val onClickHistoryTrack = object : ClickListernForTrack {
            override fun onTrackClickListern(track: Track) {
                val position = adapterHistory.tracks.indexOf(track)
                trackListFromSharedPerf.remove(track)
                searchHistory.saveTrackToJson(trackListFromSharedPerf)
                adapterHistory.notifyItemRemoved(position)
                adapterHistory.notifyItemRangeChanged(position, adapterHistory.tracks.size)

                if (adapterHistory.tracks.isNullOrEmpty()) {
                historySearchHead.visibility = View.GONE
                } else{
                    historySearchHead.visibility = View.VISIBLE
                }
            }
        }
        adapterHistory.clickListern = onClickHistoryTrack

// Clear History Button
        clearHistoryButton.setOnClickListener {
            searchHistory.clearHistory()
            adapterHistory.tracks.clear()
            historySearchHead.isVisible = false
            adapterHistory.notifyDataSetChanged()
        }
        //Вешаем на строку ввода слушателя событий
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val querySearch = inputEditText.text.toString().trim()
                searchInApi(querySearch)
                // кнопка обновить
                updateButton?.setOnClickListener {
                    searchInApi(querySearch)
                }
            }
            true
        }
    }

    private fun searchInApi(querySearch: String) {
        if (querySearch.isNotEmpty()) {
            val call = searchService.search(querySearch)
            call.enqueue(object : Callback<TrackResponse> {

                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        adapter.tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            adapter.tracks.addAll(response.body()?.results!!)
                            adapter.notifyDataSetChanged()
                        }
                        if (adapter.tracks.isEmpty()) {
                            adapter.tracks.clear()
                            placeholderHead?.visibility = View.VISIBLE
                            placeholderMessage?.text = getString(R.string.error_empty_search)
                            imagePH?.setImageResource(R.drawable.error_smile_empty)
                            adapter.notifyDataSetChanged()
                        }
                    } else {
                        adapter.tracks.clear()
                        placeholderHead?.visibility = View.VISIBLE
                        updateButton?.visibility = View.VISIBLE
                        placeholderMessage?.text = getString(R.string.error_internet)
                        imagePH?.setImageResource(R.drawable.error_internet)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    adapter.tracks.clear()
                    placeholderHead?.visibility = View.VISIBLE
                    updateButton?.visibility = View.VISIBLE
                    placeholderMessage?.text = getString(R.string.error_internet)
                    imagePH?.setImageResource(R.drawable.error_internet)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(applicationContext, "Нет интернета", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
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

    companion object {
        private const val QUERY_VALUE = "QUERY_VALUE"
        const val SHARED_PREF_ITEM = "SHARED_PREF_KEY"
    }
}


