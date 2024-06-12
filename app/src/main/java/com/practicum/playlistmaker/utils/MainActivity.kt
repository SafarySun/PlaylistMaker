package com.practicum.playlistmaker.utils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.MediaLibraryActivity
import com.practicum.playlistmaker.search.ui.SearchActivity
import com.practicum.playlistmaker.settings.ui.SettingsActivity

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val search = findViewById<MaterialButton>(R.id.searchButton)
        search.setOnClickListener {
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }
        val mediaLibrary = findViewById<MaterialButton>(R.id.libraryButton)
        mediaLibrary.setOnClickListener {
            val mediaLibraryIntent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(mediaLibraryIntent)
        }
        val settingApp = findViewById<MaterialButton>(R.id.settingsButton)
        settingApp.setOnClickListener {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}

//image.setBackgroundColor(getColor(R.color.black))
//image.scaleType = ImageView.ScaleType.CENTER_CROP
//image.setImageResource(R.drawable.amster)