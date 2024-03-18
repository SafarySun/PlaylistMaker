package com.practicum.playlistmaker.search.presentation.ui
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.search.domain.models.Track

class SearchHistory(private  val sharedPref:SharedPreferences) {

// сохраняем треки в ШП
 private val savedHistory = loadTracks()
   fun saveTrackToJson(track:ArrayList<Track>){
        val jsonTracks = Gson().toJson(track)
        sharedPref.edit()
            .putString(HISTORY_KEY,jsonTracks)
            .apply()
    }

// Достаем треки из ШП конвертируем в Обьекты
    fun loadTracks() : ArrayList<Track> {
    val jsonHistory = sharedPref.getString(HISTORY_KEY, null)
    return jsonHistory?.let {
        ArrayList(Gson().fromJson(it, Array<Track>::class.java).toList())
    } ?: arrayListOf()
}

    // проверяем обьекты в ШП и добавляем их в историю
    fun addTrackToHistory(track: Track){
        if(savedHistory.size >= MAX_INDEX) {
            savedHistory.removeAt(savedHistory.size -1)
        }
        savedHistory.removeIf { it.trackId == track.trackId }
        savedHistory.add(0,track)
        saveTrackToJson(savedHistory)
    }

    //очищаем историю
    fun clearHistory(){
        sharedPref.edit()
            .remove(HISTORY_KEY)
            .apply()
    }



    companion object{
        const val HISTORY_KEY = "SEARCH_HISTORY_KEY"
        private const val MAX_INDEX = 10
    }

}
