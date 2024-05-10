package com.practicum.playlistmaker.sharing.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.model.EmailData


class ExternalNavigatorImpl(private val context:Context): ExternalNavigator {

    override fun shareLink(adressApp: String) {
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(adressApp)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }

    override fun openLink(agreementText: String) {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, agreementText)
            type = "text/plain"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }

    override fun openEmail(model:EmailData) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(model.email))
            putExtra(Intent.EXTRA_SUBJECT , model.subject )
            putExtra(Intent.EXTRA_TEXT, model.text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)

        }
    }
}