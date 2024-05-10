package com.practicum.playlistmaker.sharing.data

import com.practicum.playlistmaker.sharing.domain.model.EmailData

interface ExternalNavigator {
    fun shareLink(adressApp:String)
    fun openLink(agreementText:String)
    fun openEmail(model: EmailData)
}