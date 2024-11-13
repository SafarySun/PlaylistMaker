package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PlayListApi {
    @GET("/search?entity=song")
   suspend fun search(@Query("term") text: String) : TrackResponse
}