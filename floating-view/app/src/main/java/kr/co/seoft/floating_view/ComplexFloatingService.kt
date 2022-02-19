package kr.co.seoft.floating_view

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.*
import kotlin.random.Random

class ComplexFloatingService : Service() {

    private val complexFloatingViewController by lazy { ComplexFloatingViewController(this) }

    private var scope = CoroutineScope(Dispatchers.Default)
    private var job: Job? = null

    private val isShowing get() = complexFloatingViewController.isShowing

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.getBooleanExtra(ACTION_SHOW, false) == true && !isShowing) {
            complexFloatingViewController.show()
            showRandomNumber()
        } else if (intent?.getBooleanExtra(ACTION_HIDE, false) == true) {
            complexFloatingViewController.hide()
            job?.cancel()
            job = null
        }
        return START_STICKY
    }

    private fun showRandomNumber() {
        if (job == null) {
            job = scope.launch {
                while (true) {
                    withContext(Dispatchers.Main) {
                        complexFloatingViewController.update(Random.nextInt(0, 10000).toString())
                    }
                    delay(1_000)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}