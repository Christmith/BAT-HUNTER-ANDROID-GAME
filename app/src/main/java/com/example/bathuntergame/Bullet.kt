package com.example.bathuntergame

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.bathuntergame.GameView.Companion.screenRatioX
import com.example.bathuntergame.GameView.Companion.screenRatioY

class Bullet(res: Resources) {
    var x = 0
    var y = 0
    var width: Int
    var height: Int
    var bullet: Bitmap

    init {
        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet)

        width = bullet.width
        height = bullet.height

        width /= 4
        height /= 4

        width *= screenRatioX.toInt()
        height *= screenRatioY.toInt()

        bullet = Bitmap.createScaledBitmap(bullet, width, height, false)
    }

    fun getCollisionShape(): Rect {
        return Rect(x, y, x + width, y + height)
    }
}
