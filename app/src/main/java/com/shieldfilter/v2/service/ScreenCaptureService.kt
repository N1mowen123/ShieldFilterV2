package com.shieldfilter.v2.service

import android.app.ForegroundService
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.shieldfilter.v2.R

class ScreenCaptureService : ForegroundService() {
    private var mediaProjection: MediaProjection? = null
    private val CHANNEL_ID = "capture_channel"

    override fun onCreate() {
        super.onCreate()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val ch = NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_LOW)
                .setName("姿态捕获服务").build()
            NotificationManagerCompat.from(this).createNotificationChannel(ch)
        }
        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("ShieldFilter‑V2 运行中")
            .setOngoing(true).build()
        startForeground(1001, notif)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val rc = intent?.getIntExtra("RESULT_CODE",-1) ?: -1
        val data = intent?.getParcelableExtra<Intent>("PROJECTION_DATA")
        val mpm = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        if(rc != -1 && data != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            mediaProjection = mpm.getMediaProjection(rc, data)
            startService(Intent(this, OverlayDrawService::class.java))
        }
        return START_STICKY
    }

    override fun onDestroy() {
        mediaProjection?.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
