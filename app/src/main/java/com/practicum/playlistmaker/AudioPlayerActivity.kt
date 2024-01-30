package com.practicum.playlistmaker


import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    var duration: TextView? = null
    var album: TextView? = null
    var year: TextView? = null
    var genre: TextView? = null
    var country: TextView? = null
    var time: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_player)

        val back = findViewById<ImageView>(R.id.backButton)
        val playAndPause = findViewById<ImageView>(R.id.btn_play_pause)
        val imAlbum = findViewById<ImageView>(R.id.image_album)
        val trackName = findViewById<TextView>(R.id.title_album)
        val nameArtist = findViewById<TextView>(R.id.title_artist)
        val addBtn = findViewById<ImageView>(R.id.btn_add)
        val likeBtn = findViewById<ImageView>(R.id.btn_favourite)
        time = findViewById(R.id.time)
        duration = findViewById(R.id.item_duration)
        album = findViewById(R.id.item_album)
        year = findViewById(R.id.item_year)
        genre = findViewById(R.id.item_genre)
        country = findViewById(R.id.item_country)

        val trackJson = intent.getStringExtra("TRANSITION")
        val track = Gson().fromJson(trackJson, Track::class.java)

        country?.text = track.country
        genre?.text = track.primaryGenreName
        year?.text = track.releaseDate
        nameArtist?.text = track.artistName
        duration?.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        time?.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        trackName?.text = track.trackName

        if (!track.collectionName.isNullOrBlank()) {
            album?.text = track.collectionName
        } else {
            album?.isVisible = false
        }

        val coverArtworkUrl = track.getCoverArtwork()

        Glide.with(this)
            .load(coverArtworkUrl)
            .centerCrop()
            .transform(RoundedCorners(16))
            .placeholder(R.drawable.placeholder_ap)
            .into(imAlbum)

        //Кнопка назад
        back.setOnClickListener {
            finish()
        }

    }

}
