package kr.co.seoft.simple_service.count.noti

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlinx.coroutines.*
import kr.co.seoft.simple_service.count.CompleteNotificationController
import kr.co.seoft.simple_service.count.CountStatus
import kr.co.seoft.simple_service.count.OnCountListener
import kr.co.seoft.simple_service.util.e
import kotlin.random.Random

class CountNotiService : Service() {

    companion object {
        const val STOP_SERVICE = "STOP_SERVICE"
        var isAliveBackgroundService = false
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    var onCountListener: OnCountListener? = null

    private var job: Job? = null

    private val randomNumber: Int
        get() = Random.nextInt(10, 20)

    private var currentCount = 0
        private set(value) {
            "currentCount $value".e()
            onCountListener?.onSecond(value)
            if (isAliveBackgroundService) notificationController.showWithUpdate(value)
            field = value
        }

    private val notificationController by lazy { CountNotificationController(this) }
    private val completeNotificationController by lazy { CompleteNotificationController(this) }

    override fun onCreate() {
        super.onCreate()
        start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        "CountNotiService::onStartCommand".e()
        if (intent?.getBooleanExtra(STOP_SERVICE, false) == true) {
            "STOP_SERVICE".e()
            stopServiceWithActivityIfNeed()
            return START_STICKY
        }
        isAliveBackgroundService = true
        return START_STICKY
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

    fun stopServiceWithActivityIfNeed() {
        onCountListener?.onFinish()
        clearJob()
        isAliveBackgroundService = false
        notificationController.hide()
        stopSelf()
    }

    private fun createCountJobWithRun() {
        job = scope.launch {
            while (true) {
                delay(1_000)
                if (currentCount == 0) {
                    stopServiceWithActivityIfNeed()
                    completeNotificationController.show()
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
        "CountNotiService::onBind".e()
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        "CountNotiService::onUnbind".e()
        return super.onUnbind(intent)
    }

    inner class CountServiceBinder : Binder() {
        val service: CountNotiService
            get() = this@CountNotiService
    }

    override fun onDestroy() {
        "CountNotiService::onDestroy".e()
        super.onDestroy()
        scope.cancel()
    }

}