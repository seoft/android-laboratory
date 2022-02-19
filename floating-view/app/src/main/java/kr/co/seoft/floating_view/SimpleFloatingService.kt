package kr.co.seoft.floating_view

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SimpleFloatingService : Service() {

    private val simpleFloatingController by lazy { SimpleFloatingViewController(this) }

    private val isShowing get() = simpleFloatingController.isShowing

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.getBooleanExtra(ACTION_SHOW, false) == true && !isShowing) {
            simpleFloatingController.show()
        } else if (intent?.getBooleanExtra(ACTION_HIDE, false) == true) {
            simpleFloatingController.hide()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}