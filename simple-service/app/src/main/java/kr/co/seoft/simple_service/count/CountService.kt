package kr.co.seoft.simple_service.count

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.*
import kr.co.seoft.simple_service.util.e
import kotlin.random.Random

class CountService : Service() {

    private val scope = CoroutineScope(Dispatchers.IO)

    var onCountListener: OnCountListener? = null

    private var job: Job? = null

    private val randomNumber: Int
        get() = Random.nextInt(10, 20)

    var currentCount = 0
        private set(value) {
            onCountListener?.onSecond(value)
            field = value
        }

    private val notificationController by lazy { CountNotificationController(this) }

    override fun onCreate() {
        super.onCreate()
        start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        "CountService::onStartCommand".e()
        showBackgroundView()
        return START_STICKY
    }

    private fun showBackgroundView() {
        notificationController.show()
    }

    private fun hideBackgroundView() {
        notificationController.hide()
    }

    fun start() {
        if (currentCount == 0) currentCount = randomNumber
        if (job == null) createCountJobWithRun()
        onCountListener?.onStatus(CountStatus.RUNNING)
    }

    fun pause() {
        clearJob()
        job = null
        onCountListener?.onStatus(CountStatus.PAUSE)
    }

    fun stopService() {
        clearJob()
        hideBackgroundView()
        stopSelf()
    }

    private fun createCountJobWithRun() {
        job = scope.launch {
            while (true) {
                delay(1_000)
                if (currentCount == 0) {
                    onCountListener?.onStatus(CountStatus.EXIT)
                    break
                }
                currentCount -= 1
            }
        }
    }

    private fun clearJob() {
        job?.cancel()
        job = null
    }

    private val binder = CountServiceBinder()

    override fun onBind(intent: Intent?): IBinder {
        "CountService::onBind".e()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        "CountService::onUnbind".e()
        return super.onUnbind(intent)
    }

    inner class CountServiceBinder : Binder() {
        val service: CountService
            get() = this@CountService
    }

    override fun onDestroy() {
        "CountService::onDestroy".e()
        super.onDestroy()
        scope.cancel()
    }

}