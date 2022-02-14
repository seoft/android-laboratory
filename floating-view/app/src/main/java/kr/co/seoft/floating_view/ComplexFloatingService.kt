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
import kotlinx.coroutines.*
import kr.co.seoft.floating_view.databinding.LayoutComplexBinding
import kotlin.math.roundToInt
import kotlin.random.Random

class ComplexFloatingService : Service() {

    private var job: Job? = null
    private var scope = CoroutineScope(Dispatchers.Default)

    private var windowManager: WindowManager? = null
    private var params: WindowManager.LayoutParams? = null
    private var complexBinding: LayoutComplexBinding? = null
    private var complexLayout: View? = null

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
        complexBinding = LayoutComplexBinding.inflate(LayoutInflater.from(this))
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
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager?.addView(complexLayout, params)
        isShowing = true

        complexBinding?.ivClose?.setOnClickListener {
            hide()
        }

        if (job == null) {
            job = scope.launch {
                while (true) {
                    withContext(Dispatchers.Main) {
                        complexBinding?.tvRandomNumber?.text = Random.nextInt(0, 10000).toString()
                    }
                    delay(1_000)
                }
            }
        }
    }

    private fun hide() {
        if (complexLayout != null) windowManager?.removeView(complexLayout)
        complexLayout = null
        params = null
        windowManager = null
        isShowing = false
        job = null
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}