package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,

) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(externalNavigator.getShareAppLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(externalNavigator.getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(externalNavigator.getSupportEmailData())
    }

    /*private fun getShareAppLink() = context.getString(R.string.adress_android)

    private fun getSupportEmailData() = EmailData(
        email = context.getString(R.string.adress_android),
        subject = context.getString(R.string.text_theme),
        text = context.getString(R.string.message_for_developers)
    )

    private fun getTermsLink() = context.getString(R.string.adress_website)

     */


}

