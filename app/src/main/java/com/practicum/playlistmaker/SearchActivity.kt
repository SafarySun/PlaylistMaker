package com.practicum.playlistmaker


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
    private var adapter = TrackAdapter()
    private lateinit var imagePH: ImageView
    private lateinit var placeholderHead: LinearLayout
    private lateinit var placeholderMessage: TextView
    private lateinit var updateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val back = findViewById<ImageView>(R.id.backButton)
        placeholderMessage = findViewById(R.id.placeholderMessage)
        placeholderHead = findViewById(R.id.placeholder_head)
        imagePH = findViewById(R.id.image_error)
        updateButton = findViewById(R.id.button_update)


        back.setOnClickListener {
            finish()
        }
        // Проверка строки
        if (savedInstanceState != null) {
            searchText = savedInstanceState.getString(QUERY_VALUE, searchText)
            inputEditText.setText(searchText)
        }
        // Фокусировка
        inputEditText.setOnClickListener {
            inputEditText.requestFocus()
        }
        //Очистить строку и убрать клаву
        clearButton.setOnClickListener {
            inputEditText.setText("")

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
            placeholderHead.visibility = View.GONE
            updateButton.visibility = View.GONE
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
            }
            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        // Вызов наблюдателя текста
        inputEditText.addTextChangedListener(simpleTextWatcher)

        //RecycleView
        val recycler = findViewById<RecyclerView>(R.id.recycleViewSearch)
        recycler.adapter = adapter


        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val querySearch = inputEditText.text.toString().trim()
                searchInApi(querySearch)

                updateButton.setOnClickListener{
                    searchInApi(querySearch)
                }
            }
            true
        }
        false
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
                            placeholderHead.visibility = View.VISIBLE
                            placeholderMessage.text = getString(R.string.error_empty_search)
                            imagePH.setImageResource(R.drawable.error_smile_empty)
                            adapter.notifyDataSetChanged()
                        }
                    } else {
                        adapter.tracks.clear()
                        placeholderHead.visibility = View.VISIBLE
                        updateButton.visibility = View.VISIBLE
                        placeholderMessage.text = getString(R.string.error_internet)
                        imagePH.setImageResource(R.drawable.error_internet)
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    adapter.tracks.clear()
                    placeholderHead.visibility = View.VISIBLE
                    updateButton.visibility = View.VISIBLE
                    placeholderMessage.text = getString(R.string.error_internet)
                    imagePH.setImageResource(R.drawable.error_internet)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(applicationContext, "Нет интернета", Toast.LENGTH_LONG)
                        .show()
                }
            })
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(QUERY_VALUE, searchText)
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
    }
}


