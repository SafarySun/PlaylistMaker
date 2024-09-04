package com.practicum.playlistmaker.media_creation.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.media_creation.view_model.PlayListCreationViewModel
import com.practicum.playlistmaker.media_creation.view_model.states.CompletionDialogState
import com.practicum.playlistmaker.utils.dpToPx
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistCreationFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlayListCreationViewModel>()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        viewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            Glide.with(binding.imageAlbum)
                .load(uri)
                .transform(CenterCrop(), RoundedCorners(dpToPx(requireContext(), 24)))
                .placeholder(R.drawable.create_photo)
                .into(binding.imageAlbum)
        }

        pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(binding.imageAlbum)
                        .load(uri.toString())
                        .transform(CenterCrop(), RoundedCorners(24))
                        .placeholder(R.drawable.create_photo)
                        .into(binding.imageAlbum)
                    viewModel.saveImageUri(uri)
                }
            }
        binding.imageAlbum.setOnClickListener {
            lifecycleScope.launch {

                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

            }
        }


        val backClick = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")              // Заголовок диалога
            .setMessage("Все несохраненные данные будут потеряны")  // Описание диалога
            //.setNeutralButton("Отмена") { dialog, which ->}       // Добавляет кнопку «Отмена»
            .setNegativeButton("Отмена") { _, _ ->
                viewModel.continueCreating()
            }
            .setPositiveButton("Завершить") { _, _ ->
                findNavController().navigateUp()

            }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.showCompletionDialog()
        }
        viewModel.getCompletionState().observe(viewLifecycleOwner) { state ->
            when (state) {
                CompletionDialogState.SHOW_DIALOG -> backClick.show()
                CompletionDialogState.FINISH -> findNavController().navigateUp()
                else -> Unit
            }
        }
        binding.createButton.setOnClickListener {
            viewModel.createPlayList()
            Toast.makeText(
                requireContext(), "Плейлист ${binding.nameEt.text} создан",
                Toast.LENGTH_LONG
            ).show()
        }

        binding.backButton.setOnClickListener {
            viewModel.showCompletionDialog()
        }

        binding.nameEt.doOnTextChanged { text, _, _, _ ->
            binding.createButton.isEnabled = (!text.isNullOrBlank())
            changeFrameColor(text, binding.nameLayout)
            viewModel.saveNameEt(text)
        }
        binding.descriptionEt.doOnTextChanged { text, _, _, _ ->
            changeFrameColor(text, binding.descriptionLayout)
            viewModel.saveDescriptionEt(text)
        }
    }

    private fun setImage(pickMedia: ActivityResultLauncher<PickVisualMediaRequest>) {
        pickMedia
            .launch(
                PickVisualMediaRequest(
                    ActivityResultContracts
                        .PickVisualMedia
                        .ImageOnly
                )
            )
    }

    private fun settings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data =
            Uri.fromParts("package", requireContext().packageName, null)
        requireContext().startActivity(intent)
    }


    private fun changeFrameColor(text: CharSequence?, view: TextInputLayout) {
        if (text.isNullOrEmpty()) {
            ContextCompat.getColorStateList(requireContext(), R.color.nofocus_grey_focuse_blue)
                ?.let {
                    view.setBoxStrokeColorStateList(it)
                    view.defaultHintTextColor = it
                }
        } else {
            ContextCompat.getColorStateList(requireContext(), R.color.blue_blue_blue)
                ?.let {
                    view.setBoxStrokeColorStateList(it)
                    view.defaultHintTextColor = it
                }
        }
    }


}


