package com.practicum.playlistmaker
import java.io.Serializable


data class Track(
    val trackId:Int,
    val trackName:String,
    val artistName:String,
    val trackTimeMillis:Long,
    val  artworkUrl100:String,
    val collectionName :String,
    val releaseDate : String,
    val primaryGenreName :String,
    val country :String) : Serializable
{
    fun getCoverArtwork(): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }
}


