package com.practicum.playlistmaker.sharing.domain.api

interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()

    fun sharePlData(sharedPlData: String)
}