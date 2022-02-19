package kr.co.seoft.floating_view

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import kr.co.seoft.floating_view.databinding.LayoutComplexBinding
import kotlin.math.roundToInt

class ComplexFloatingViewController(private val context: Context) {

    private var windowManager: WindowManager? = null
    private var params: WindowManager.LayoutParams? = null
    private var complexBinding: LayoutComplexBinding? = null
    private var complexLayout: View? = null

    private var initX = 0
    private var initY = 0
    private var initTouchX = 0f
    private var initTouchY = 0f

    var isShowing = false

    @SuppressLint("ClickableViewAccessibility", "RtlHardcoded")
    fun show() {
        complexBinding = LayoutComplexBinding.inflate(LayoutInflater.from(context))
        complexLayout = complexBinding?.root
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        complexLayout?.rootView?.setOnTouchListener { v, event ->

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
                    windowManager?.updateViewLayout(complexLayout, params)
                    return@setOnTouchListener true
                }
            }
            false
        }
        windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        windowManager?.addView(complexLayout, params)
        isShowing = true

        complexBinding?.ivClose?.setOnClickListener {
            hide()
        }

    }

    fun update(number: String) {
        complexBinding?.tvRandomNumber?.text = number
    }

    fun hide() {
        if (complexLayout != null) windowManager?.removeView(complexLayout)
        complexLayout = null
        params = null
        windowManager = null
        isShowing = false
    }

}