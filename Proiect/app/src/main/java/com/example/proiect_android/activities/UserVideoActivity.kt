package com.example.proiect_android.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import com.example.proiect_android.R
import com.google.android.material.button.MaterialButton

class UserVideoActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var playButton : MaterialButton
    private val videoUriKey = "videoUri"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_video)

        videoView = findViewById(R.id.user_video)
        playButton = findViewById(R.id.play_button)

        val bundle : Bundle? = intent.extras
        val uri = bundle!!.getString(videoUriKey)

        Log.d("STATE", uri.toString())
        val offlineUri = Uri.parse(uri)
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        videoView.setVideoURI(offlineUri)

        playButton.setOnClickListener{
            playButton.visibility = View.INVISIBLE
            videoView.setMediaController(mediaController)
            videoView.start()
        }
    }
}