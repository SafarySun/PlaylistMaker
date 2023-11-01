package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings1)

        val back = findViewById<ImageView>(R.id.backButton)

        back.setOnClickListener{
            val backIntent = Intent(this,MainActivity::class.java)
            startActivity(backIntent)
        }
    }
}