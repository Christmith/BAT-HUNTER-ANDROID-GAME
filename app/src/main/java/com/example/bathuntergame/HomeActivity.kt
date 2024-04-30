package com.example.bathuntergame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class HomeActivity : AppCompatActivity() {
    private var isMute = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_home)

        findViewById<View>(R.id.playButton).setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity,
                    GameActivity::class.java
                )
            )
        }

        val highScoreText = findViewById<TextView>(R.id.highScoreText)
        val prefs = getSharedPreferences("game", MODE_PRIVATE)
        highScoreText.text = "HighScore:" + prefs.getInt("highScore", 0)

        isMute = prefs.getBoolean("isMute", false)
        val volumeCtrl = findViewById<ImageView>(R.id.volumeCtrl)

        if (isMute) volumeCtrl.setImageResource(R.drawable.baseline_volume_up_24) else {
            volumeCtrl.setImageResource(R.drawable.baseline_volume_off_24)
        }

        volumeCtrl.setOnClickListener {
            isMute = !isMute
            if (isMute) volumeCtrl.setImageResource(R.drawable.baseline_volume_up_24) else {
                volumeCtrl.setImageResource(R.drawable.baseline_volume_off_24)
            }

            val editor = prefs.edit()
            editor.putBoolean("isMute", isMute)
            editor.apply()
        }
    }
}

