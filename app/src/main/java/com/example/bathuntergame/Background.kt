package com.example.bathuntergame

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Background(screenX: Int, screenY: Int, res: Resources) {
    var x = 0
    var y = 0
    var background: Bitmap

    init {
        background = BitmapFactory.decodeResource(res, R.drawable.bg_2)
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false)
    }
}
