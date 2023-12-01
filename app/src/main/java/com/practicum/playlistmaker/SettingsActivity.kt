package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle

import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.widget.ImageView
import android.widget.Switch

class SettingsActivity : AppCompatActivity() {
    private lateinit var sharedPrefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //Кнопка назад
        val back = findViewById<ImageView>(R.id.backButton)
        back.setOnClickListener {
            finish()
        }
        //Переключение режима Ночной-Дневной
        val switchTheme = findViewById<Switch>(R.id.btn_switch_theme)
        sharedPrefs =
            applicationContext.getSharedPreferences("Playlist maker", Context.MODE_PRIVATE)
        switchTheme.isChecked = isDarkThemeEnabled()
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            saveThemeState(isChecked)
        }
        //Реализовать кнопку «Написать в поддержку»:
        val writeSupport = findViewById<FrameLayout>(R.id.btn_write_support)
        writeSupport.setOnClickListener {

            val messageIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.adress_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.text_theme))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.message_for_developers))
                startActivity(this)
            }
        }
        //Поделиться приложением
        val sendIntent = findViewById<FrameLayout>(R.id.btn_share_app)
        sendIntent.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.adress_android)
            )
            shareIntent.setType("text/plain")
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
        }
        //Пользовательское Соглашение
        val sendAdress = findViewById<FrameLayout>(R.id.btn_ageement)
        sendAdress.setOnClickListener {
            val userAgreementIntent = Intent(Intent.ACTION_VIEW)
            val adressWeb = getString(R.string.adress_website)
            userAgreementIntent.data = Uri.parse(adressWeb)
            startActivity(userAgreementIntent)
        }
    }

    private fun isDarkThemeEnabled(): Boolean {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }

    private fun saveThemeState(isDarkThemeEnabled: Boolean) {
        val editor = sharedPrefs.edit()
        editor.putBoolean(DARK_ON, isDarkThemeEnabled)
        editor.apply()
    }

    companion object {
        private const val DARK_ON = "is_dark_theme_enabled"
    }
}