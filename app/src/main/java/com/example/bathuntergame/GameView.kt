package com.example.bathuntergame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.view.MotionEvent
import android.view.SurfaceView
import java.util.Random


class GameView(private val gameActivity: GameActivity, screenX: Int, screenY: Int) : SurfaceView(
    gameActivity
),
    Runnable {
    private var thread: Thread? = null
    private var isPlaying = false
    private var isGameOver = false
    private val screenX: Int
    private val screenY: Int
    private var score = 0
    private val paint: Paint
    private val bats: Array<Bat?>
    private val random: Random
    private val bullets: MutableList<Bullet>
    private val flight: Flight
    private val background1: Background
    private val background2: Background
    private val prefs: SharedPreferences
    private var soundPool: SoundPool? = null
    private val sound: Int

    init {

        prefs = gameActivity.getSharedPreferences("game", Context.MODE_PRIVATE)

        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build()
            SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build()
        } else SoundPool(1, AudioManager.STREAM_MUSIC, 0)

        sound = soundPool!!.load(gameActivity, R.raw.gunshot, 1)

        this.screenX = screenX
        this.screenY = screenY

        screenRatioX = 2400f / screenX
        screenRatioY = 1080f / screenY

        background1 = Background(screenX, screenY, resources)
        background2 = Background(screenX, screenY, resources)

        flight = Flight(this, screenY, resources)

        bullets = ArrayList()

        background2.x = screenX

        paint = Paint()

        paint.textSize = 128f
        paint.color = Color.WHITE

        bats = arrayOfNulls(7)
        for (i in 0..6) {
            val bat = Bat(resources)
            bats[i] = bat
        }

        random = Random()
    }

    override fun run() {
        while (isPlaying) {
            update()
            draw()
            sleep()
        }
    }

    private fun update() {
        background1.x = (background1.x - 20 * screenRatioX).toInt()
        background2.x = (background2.x - 20 * screenRatioX).toInt()

        if (background1.x + background1.background.width < 0) {
            background1.x = screenX
        }

        if (background2.x + background2.background.width < 0) {
            background2.x = screenX
        }

        if (flight.isGoingUp) {
            flight.y = (flight.y - 30 * screenRatioY).toInt()
        } else flight.y = (flight.y + 20 * screenRatioY).toInt()

        if (flight.y < 0) flight.y = 0

        val trash: MutableList<Bullet> = ArrayList()

        if (flight.y > screenY - flight.height) flight.y = screenY - flight.height

        for (bullet in bullets) {
            if (bullet.x > screenX) trash.add(bullet)
            bullet.x = (bullet.x + 100 * screenRatioX).toInt()
            for (bat in bats) {
                if (Rect.intersects(bat!!.getCollisionShape(), bullet.getCollisionShape())) {
                    score++
                    bat.x = -500
                    bullet.x = screenX + 500
                    bat.wasShot = true
                }
            }
        }

        for (bullet in trash) bullets.remove(bullet)

        for (bat in bats) {
            bat!!.x -= bat!!.speed
            if (bat.x + bat.width < 0) {
                if (!bat.wasShot) {
                    isGameOver = true
                    return
                }
                val bound = (32 * screenRatioX).toInt()
                bat.speed = random.nextInt(bound)
                if (bat.speed < 10 * screenRatioX) bat.speed = (10 * screenRatioX).toInt()
                bat.x = screenX
                bat.y = random.nextInt(screenY - bat.height)
                bat.wasShot = false
            }
            if (Rect.intersects(bat.getCollisionShape(), flight.getCollisionShape())) {
                isGameOver = true
                return
            }
        }
    }

    private fun draw() {
        if (holder.surface.isValid) {

            val canvas = holder.lockCanvas()

            canvas.drawBitmap(
                background1.background,
                background1.x.toFloat(),
                background1.y.toFloat(),
                paint
            )

            canvas.drawBitmap(
                background2.background,
                background2.x.toFloat(),
                background2.y.toFloat(),
                paint
            )

            for (bat in bats) canvas.drawBitmap(
                bat!!.getBat(),
                bat.x.toFloat(),
                bat.y.toFloat(),
                paint
            )

            canvas.drawText(score.toString() + "", screenX / 2f, 164f, paint)

            if (isGameOver) {
                isPlaying = false
                val textWidth = paint.measureText("Game Over")
                val x = (screenX - textWidth) / 2f // Center horizontally
                val y = (screenY - paint.textSize) / 2f // Center vertically
                canvas.drawText("Game Over", x, y, paint)
                canvas.drawBitmap(flight.getDeadBitmap(), flight.x.toFloat(), flight.y.toFloat(), paint)
                holder.unlockCanvasAndPost(canvas)
                saveIfHighScore()
                waitBeforeExiting()
                return
            }

            for (bat in bats) canvas.drawBitmap(
                bat!!.getBat(),
                bat.x.toFloat(),
                bat.y.toFloat(),
                paint
            )

            canvas.drawBitmap(flight.getFlight(), flight.x.toFloat(), flight.y.toFloat(), paint)

            for (bullet in bullets) canvas.drawBitmap(
                bullet.bullet,
                bullet.x.toFloat(),
                bullet.y.toFloat(),
                paint
            )
            holder.unlockCanvasAndPost(canvas)
        }
    }

    private fun waitBeforeExiting() {
        try {
            Thread.sleep(3000)
            gameActivity.startActivity(Intent(gameActivity, HomeActivity::class.java))
            gameActivity.finish()
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }

    private fun saveIfHighScore() {
        if (prefs.getInt("highScore", 0) < score) {
            val editor = prefs.edit()
            editor.putInt("highScore", score)
            editor.apply()
        }
    }

    private fun sleep() {
        try {
            Thread.sleep(20)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }

    fun resume() {
        isPlaying = true
        thread = Thread(this)
        thread!!.start()
    }

    fun pause() {
        try {
            isPlaying = false
            thread!!.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (event.x < screenX / 2) {
                flight.isGoingUp = true
            }

            MotionEvent.ACTION_UP -> {
                flight.isGoingUp = false
                if (event.x > screenX / 2) {
                    flight.toShoot++
                }
            }
        }
        return true
    }

    fun newBullet() {
        if (prefs.getBoolean("isMute", false)) soundPool!!.play(sound, 1f, 1f, 0, 0, 1f)
        val bullet = Bullet(resources)
        bullet.x = flight.x + flight.width
        bullet.y = flight.y + flight.height / 2
        bullets.add(bullet)
    }

    companion object {
        var screenRatioX: Float = 2400f
        var screenRatioY: Float = 1080f
    }
}

