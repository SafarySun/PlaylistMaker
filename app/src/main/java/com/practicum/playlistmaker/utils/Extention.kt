package com.practicum.playlistmaker.utils
import android.content.Context
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.Locale

// Расширение для форматирования длительности трека
fun formatDuration(time: Long): String {
    return  SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
}

// Расширение для форматирования года релиза трека
fun String.formatYear(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val parsedDate = formatter.parse(this)
    val outputFormatter = SimpleDateFormat("yyyy", Locale.getDefault())
    return outputFormatter.format(parsedDate)
}
fun dpToPx(context: Context, dp: Int): Int {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), metrics).toInt()
}
