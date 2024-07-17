package com.practicum.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TrackRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val context: Context,
    private val playListApi:PlayListApi) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (!isConected()) {
            return Response().apply { resultCode = NO_INTERNET }
        }
        return if (dto !is TrackRequest) {
            Response().apply { resultCode = ERROR_SERVER }
        } else {
           return withContext(Dispatchers.IO){
               try{
                   val resp = playListApi.search(dto.expression)
                   resp.apply { resultCode = SUCCESS }

               } catch (e: Throwable) {
                   Response().apply { resultCode = ERROR }
               }
            }
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
    companion object{
        private const val NO_INTERNET = -1
        private const val ERROR_SERVER = 400
        private const val SUCCESS = 200
        private const val ERROR = 500
    }
}
