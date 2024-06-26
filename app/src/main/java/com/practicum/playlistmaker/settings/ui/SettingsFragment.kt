package com.practicum.playlistmaker.settings.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSettingsLiveData().observe(viewLifecycleOwner) {
            binding.btnSwitchTheme.isChecked = it
        }


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





