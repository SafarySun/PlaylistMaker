package com.practicum.playlistmaker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        //Кнопка назад
        val back = findViewById<ImageView>(R.id.backButton)
        back.setOnClickListener {
            finish()
        }


        //Переключение режима Ночной-Дневной

        val switchTheme = findViewById<SwitchMaterial>(R.id.btn_switch_theme)

        switchTheme.isChecked = (applicationContext as App).darkTheme

        switchTheme.setOnCheckedChangeListener { switcher, checked ->

            (applicationContext as App).switchTheme(checked)
            (applicationContext as App).saveState()

        }


        //Реализовать кнопку «Написать в поддержку»:
        val writeSupport = findViewById<FrameLayout>(R.id.btn_write_support)
        writeSupport.setOnClickListener {

            Intent(Intent.ACTION_SENDTO).apply {
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
            shareIntent.type = "text/plain"
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


    }





