package com.practicum.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TrackRequest

class RetrofitNetworkClient(
    private val context: Context,
    private val playListApi:PlayListApi) : NetworkClient {

    override fun doRequest(dto: Any): Response {
        if (!isConected()) {
            return Response().apply { resultCode = -1 }
        }
        return if (dto !is TrackRequest) {
            Response().apply { resultCode = 400 }
        } else {
            val resp = playListApi.search(dto.expression).execute()

            val body = resp.body() ?: Response()

            body.apply { resultCode = resp.code() }

        }
    }



    private fun isConected(): Boolean{
        val conectivityServis = context.getSystemService(Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities = conectivityServis.getNetworkCapabilities(conectivityServis.activeNetwork)
        if(capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}