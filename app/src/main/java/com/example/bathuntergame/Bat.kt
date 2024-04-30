package com.example.bathuntergame

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.bathuntergame.GameView.Companion.screenRatioX
import com.example.bathuntergame.GameView.Companion.screenRatioY

class Bat(res: Resources) {
    var speed = 30
    var wasShot = true
    var x = 0
    var y: Int
    var width: Int
    var height: Int
    var batCounter = 1
    var bat1: Bitmap
    var bat2: Bitmap
    var bat3: Bitmap
    var bat4: Bitmap
    var bat5: Bitmap
    var bat6: Bitmap
    var explosion: Bitmap? = null

    init {
        bat1 = BitmapFactory.decodeResource(res, R.drawable.animated_flying_bat_1)
        bat2 = BitmapFactory.decodeResource(res, R.drawable.animated_flying_bat_2)
        bat3 = BitmapFactory.decodeResource(res, R.drawable.animated_flying_bat_3)
        bat4 = BitmapFactory.decodeResource(res, R.drawable.animated_flying_bat_4)
        bat5 = BitmapFactory.decodeResource(res, R.drawable.animated_flying_bat_5)
        bat6 = BitmapFactory.decodeResource(res, R.drawable.animated_flying_bat_6)

        width = bat1.width
        height = bat1.height

        width /= 4
        height /= 4

        width *= screenRatioX.toInt()
        height *= screenRatioY.toInt()

        bat1 = Bitmap.createScaledBitmap(bat1, width, height, false)
        bat2 = Bitmap.createScaledBitmap(bat2, width, height, false)
        bat3 = Bitmap.createScaledBitmap(bat3, width, height, false)
        bat4 = Bitmap.createScaledBitmap(bat4, width, height, false)
        bat5 = Bitmap.createScaledBitmap(bat5, width, height, false)
        bat6 = Bitmap.createScaledBitmap(bat6, width, height, false)

        y = -height
    }

    fun getBat(): Bitmap {
        return when (batCounter) {
            1 -> {
                batCounter++
                bat1
            }
            2 -> {
                batCounter++
                bat2
            }
            3 -> {
                batCounter++
                bat3
            }
            4 -> {
                batCounter++
                bat4
            }
            5 -> {
                batCounter++
                bat5
            }
            6 -> {
                batCounter++
                bat6
            }
            7 -> {
                batCounter++
                bat5
            }
            8 -> {
                batCounter++
                bat4
            }
            9 -> {
                batCounter++
                bat3
            }
            else -> {
                batCounter = 1
                bat2
            }
        }
    }

    fun getCollisionShape(): Rect {
        return Rect(x, y, x + width, y + height)
    }

    fun getHit(): Bitmap? {
        return explosion
    }
}
