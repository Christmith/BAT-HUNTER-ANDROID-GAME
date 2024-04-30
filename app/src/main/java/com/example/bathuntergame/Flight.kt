package com.example.bathuntergame

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import com.example.bathuntergame.GameView.Companion.screenRatioX
import com.example.bathuntergame.GameView.Companion.screenRatioY
import com.example.bathuntergame.R

class Flight(private val gameView: GameView, screenY: Int, res: Resources) {
    var toShoot = 0
    var isGoingUp = false
    var x: Int
    var y: Int
    var width: Int
    var height: Int
    var wingCounter = 0
    var shootCounter = 1
    var flight1: Bitmap
    var flight2: Bitmap
    var shoot1: Bitmap
    var shoot2: Bitmap
    var shoot3: Bitmap
    var shoot4: Bitmap
    var shoot5: Bitmap
    var dead: Bitmap

    init {
        flight1 = BitmapFactory.decodeResource(res, R.drawable.fly_1)
        flight2 = BitmapFactory.decodeResource(res, R.drawable.fly_2)

        width = flight1.width
        height = flight1.height

        width /= 4
        height /= 4

        width *= screenRatioX.toInt()
        height *= screenRatioY.toInt()

        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false)
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false)

        y = screenY / 2
        x = (64 * screenRatioX).toInt()

        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot_1)
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot_2)
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot_3)
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot_4)
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot_5)

        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false)
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false)
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false)
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false)
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false)

        dead = BitmapFactory.decodeResource(res, R.drawable.dead)
        dead = Bitmap.createScaledBitmap(dead, width, height, false)
    }

    fun getFlight(): Bitmap {
        if (toShoot != 0) {
            when (shootCounter) {
                1 -> {
                    shootCounter++
                    return shoot1
                }
                2 -> {
                    shootCounter++
                    return shoot2
                }
                3 -> {
                    shootCounter++
                    return shoot3
                }
                4 -> {
                    shootCounter++
                    return shoot4
                }
                else -> {
                    shootCounter = 1
                    toShoot--
                    gameView.newBullet()
                    return shoot5
                }
            }
        }

        if (wingCounter == 0) {
            wingCounter++
            return flight1
        }

        wingCounter--
        return flight2
    }

    fun getCollisionShape(): Rect {
        return Rect(x, y, x + width, y + height)
    }

    fun getDeadBitmap(): Bitmap {
        return dead
    }
}
