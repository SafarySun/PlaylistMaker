package com.practicum.playlistmaker
import java.text.SimpleDateFormat
import java.util.Locale

// Расширение для форматирования длительности трека
fun Track.formatDuration(time:Long): String {
    return  SimpleDateFormat("mm:ss", Locale.getDefault()).format(time).toString()
}

// Расширение для форматирования года релиза трека
fun String.formatYear(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val parsedDate = formatter.parse(this)
    val outputFormatter = SimpleDateFormat("yyyy", Locale.getDefault())
    return outputFormatter.format(parsedDate)
}
