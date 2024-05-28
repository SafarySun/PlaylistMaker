package com.practicum.playlistmaker.search.data.sharedprefs
import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.search.domain.models.Track

class LocalStorageImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson:Gson)
    : LocalStorage {


// сохраняем треки в ШП
 private val savedHistory = loadTracks()
   private fun saveTrackToJson(track:ArrayList<Track>){
        val jsonTracks = gson.toJson(track)
        sharedPreferences.edit()
            .putString(HISTORY_KEY,jsonTracks)
            .apply()
    }

// Достаем треки из ШП конвертируем в Обьекты
    override fun loadTracks() : ArrayList<Track> {
    val jsonHistory = sharedPreferences.getString(HISTORY_KEY, null)
    return jsonHistory?.let {
        ArrayList(gson.fromJson(it, Array<Track>::class.java).toList())
    } ?: arrayListOf()
}

    // проверяем обьекты в ШП и добавляем их в историю
    override fun addTrackToHistory(track: Track){
        if(savedHistory.size >= MAX_INDEX) {
            savedHistory.removeAt(savedHistory.size -1)
        }
        savedHistory.removeIf { it.trackId == track.trackId }
        savedHistory.add(0,track)
        saveTrackToJson(savedHistory)
    }

    //очищаем историю
    override fun clearHistory(){
        sharedPreferences.edit()
            .clear()
            .remove(HISTORY_KEY)
            .apply()
    }



    companion object{
        const val HISTORY_KEY = "SEARCH_HISTORY_KEY"
        private const val MAX_INDEX = 10
    }

}
