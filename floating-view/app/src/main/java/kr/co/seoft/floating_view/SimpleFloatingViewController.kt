package kr.co.seoft.floating_view

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import kr.co.seoft.floating_view.databinding.LayoutSimpleBinding
import kotlin.math.roundToInt

class SimpleFloatingViewController(private val context: Context) {

    private var windowManager: WindowManager? = null
    private var params: WindowManager.LayoutParams? = null
    private var simpleLayout: View? = null

    private var initX = 0
    private var initY = 0
    private var initTouchX = 0f
    private var initTouchY = 0f

    var isShowing = false

    @SuppressLint("ClickableViewAccessibility", "RtlHardcoded")
    fun show() {
        simpleLayout = LayoutSimpleBinding.inflate(LayoutInflater.from(context)).root
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
        windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        windowManager?.addView(simpleLayout, params)
        isShowing = true
    }

    fun hide() {
        if (simpleLayout != null) windowManager?.removeView(simpleLayout)
        simpleLayout = null
        params = null
        windowManager = null
        isShowing = false
    }
}