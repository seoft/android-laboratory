package kr.co.seoft.floating_view

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import kr.co.seoft.floating_view.databinding.LayoutSimpleBinding
import kotlin.math.roundToInt

class SimpleFloatingService : Service() {

    private var windowManager: WindowManager? = null
    private var params: WindowManager.LayoutParams? = null
    private var simpleLayout: View? = null

    private var initX = 0
    private var initY = 0
    private var initTouchX = 0f
    private var initTouchY = 0f

    private var isShowing = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.getBooleanExtra(ACTION_SHOW, false) == true && !isShowing) show()
        if (intent?.getBooleanExtra(ACTION_HIDE, false) == true) hide()
        return START_STICKY
    }

    @SuppressLint("ClickableViewAccessibility", "RtlHardcoded")
    private fun show() {
        simpleLayout = LayoutSimpleBinding.inflate(LayoutInflater.from(this)).root
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        simpleLayout?.rootView?.setOnTouchListener { v, event ->

            fun calcX() = initX + (event.rawX - initTouchX).roundToInt()
            fun calcY() = initY + (event.rawY - initTouchY).roundToInt()

            val stableParams = params ?: return@setOnTouchListener false
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initX = stableParams.x
                    initY = stableParams.y
                    initTouchX = event.rawX
                    initTouchY = event.rawY
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
                    params?.x = calcX()
                    params?.y = calcY()
                    windowManager?.updateViewLayout(simpleLayout, params)
                    return@setOnTouchListener true
                }
            }
            false
        }
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager?.addView(simpleLayout, params)
        isShowing = true
    }

    private fun hide() {
        if (simpleLayout != null) windowManager?.removeView(simpleLayout)
        simpleLayout = null
        params = null
        windowManager = null
        isShowing = false
    }

    override fun onBind(intent: Intent?): IBinder? = null
}