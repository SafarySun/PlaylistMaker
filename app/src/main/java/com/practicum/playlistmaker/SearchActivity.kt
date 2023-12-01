package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    private var searchText = "VALUE_DEF"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val back = findViewById<ImageView>(R.id.backButton)
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
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(QUERY_VALUE, searchText)
    }
     override fun onRestoreInstanceState(savedInstanceState: Bundle) {
       super.onRestoreInstanceState(savedInstanceState)
     searchText = savedInstanceState.getString(QUERY_VALUE,  "")
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