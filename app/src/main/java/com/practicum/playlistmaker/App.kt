package com.practicum.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    private lateinit var sharedPrefs:SharedPreferences
    var darkTheme = false
    override fun onCreate() {
        super.onCreate()
        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)

        darkTheme = sharedPrefs.getBoolean(KEY, false)

        switchTheme(darkTheme)

    }
    fun switchTheme(darkThemeEnable:Boolean){
        darkTheme = darkThemeEnable

        AppCompatDelegate.setDefaultNightMode(
            if(darkThemeEnable){
                AppCompatDelegate.MODE_NIGHT_YES
            }else{
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

    }
     fun saveState(){
         sharedPrefs.edit()
             .putBoolean(KEY,darkTheme)
             .apply()
         Toast.makeText(this, "", Toast.LENGTH_SHORT)
             .show()

     }
    companion object{
        const val PREFERENCES = "preferencies"
        const val KEY = "key_for_theme"
    }
}