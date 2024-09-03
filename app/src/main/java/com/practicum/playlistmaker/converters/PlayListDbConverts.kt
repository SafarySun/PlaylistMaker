package com.practicum.playlistmaker.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.media_creation.data.db.entity.PlayListEntity
import com.practicum.playlistmaker.media_creation.domain.model.PlayList

class PlayListDbConverts {
    fun map(playList: PlayList): PlayListEntity {
        return PlayListEntity(
            playList.playlistId,
            playList.name,
            playList.description,
            playList.coverImage,
            Gson().toJson(playList.tracksId) ,
            playList.amountTracks
        )
    }

    fun map(playList: PlayListEntity): PlayList {
        val trackIdsType = object : TypeToken<List<Int>>() {}.type
        val tracksIdList: ArrayList<Int> = Gson().fromJson(playList.tracksId, trackIdsType) ?: arrayListOf()

        return PlayList(
            playList.playlistId,
            playList.name,
            playList.discription,
            playList.coverImage,
            tracksIdList,
            playList.amountTracks
        )
    }
}


