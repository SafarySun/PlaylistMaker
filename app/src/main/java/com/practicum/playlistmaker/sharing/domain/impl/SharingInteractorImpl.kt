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

    override fun sharePlData(sharedPlData: String) {
        externalNavigator.shareLink(sharedPlData)
    }

}

