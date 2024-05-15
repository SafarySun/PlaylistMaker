package com.practicum.playlistmaker.settings.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SettingsViewModel.getViewModelFactory(applicationContext)
        )[SettingsViewModel::class.java]
    }

    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSettingsLiveData().observe(this) {
            binding.btnSwitchTheme.isChecked = it
        }
        //Кнопка назад
        binding.backButton.setOnClickListener {
            finish()
        }

        //Переключение режима Ночной-Дневной

        binding.btnSwitchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.switchTheme(isChecked)
        }

        //Реализовать кнопку «Написать в поддержку»:
        binding.btnWriteSupport.setOnClickListener {
            viewModel.openSupport()
        }

        //Поделиться приложением
        binding.btnShareApp.setOnClickListener {
            viewModel.shareApp()
        }
        //Пользовательское Соглашение
        binding.btnAgeement.setOnClickListener {
            viewModel.openTerms()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.updateThemeSetting()
    }


}





