package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData
import com.practicum.playlistmaker.utils.creator.Creator

class SharingInteractorImpl(private val externalNavigator: ExternalNavigator) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareAppLink() = Creator.application.getString(R.string.adress_android)

    private fun getSupportEmailData() = EmailData(
        email =  Creator.application.getString(R.string.adress_android),
        subject =  Creator.application.getString(R.string.text_theme),
        text =  Creator.application.getString(R.string.message_for_developers)
    )

    private fun getTermsLink() = Creator.application.getString(R.string.adress_website)


}

