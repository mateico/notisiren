package com.example.notisiren

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

object AlarmUtils {

    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler? = null
    private var stopRunnable: Runnable? = null
    private var isPlaying = false

    fun startAlarm(context: Context) {
        if (isPlaying) return  // Prevent duplicate alarms

        isPlaying = true
        mediaPlayer = MediaPlayer.create(context.applicationContext, R.raw.alarm)
        mediaPlayer?.setOnCompletionListener {
            if (isPlaying) {
                it.seekTo(0)
                it.start()
            }
        }
        mediaPlayer?.start()

        // Stop after 3 minutes (180000 ms)
        handler = Handler(Looper.getMainLooper())
        stopRunnable = Runnable {
            stopAlarm()
        }
        handler?.postDelayed(stopRunnable!!, 3 * 60 * 1000)
    }

    fun stopAlarm() {
        isPlaying = false
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        handler?.removeCallbacks(stopRunnable ?: Runnable {})
        handler = null
        stopRunnable = null
    }
}