package com.shieldfilter.v2.service

import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager
import android.widget.FrameLayout
import com.shieldfilter.v2.overlay.OverlayDrawer

class OverlayDrawService : Service() {
    private var wm: WindowManager? = null
    private var overlay: FrameLayout? = null
    private val drawer = OverlayDrawer()

    override fun onCreate() {
        super.onCreate()
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        overlay = FrameLayout(this)
        val lp = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.FILL }
        wm?.addView(overlay, lp)
    }

    override fun onDestroy() {
        overlay?.let { wm?.removeView(it) }
        super.onDestroy()
    }

    override fun onBind(i: Intent?): IBinder? = null
}
