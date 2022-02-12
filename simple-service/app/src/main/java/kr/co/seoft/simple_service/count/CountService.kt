package kr.co.seoft.simple_service.count

import android.app.Service
import android.content.Intent
import android.os.Binder
import kotlinx.coroutines.*
import kotlin.random.Random

class CountService : Service() {

    private val scope = CoroutineScope(Dispatchers.IO)

    var onCountListener: OnCountListener? = null

    var job: Job? = null

    private val randomNumber: Int
        get() = Random.nextInt(2, 10)

    private var currentCount = 0
        set(value) {
            onCountListener?.onSecond(value)
            field = value
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

    fun stop() {
        clearJob()
        currentCount = 0
        onCountListener?.onStatus(CountStatus.STOP)
    }

    private fun createCountJobWithRun() {
        job = scope.launch {
            while (true) {
                delay(1_000)
                if (currentCount == 0) break
                currentCount -= 1
            }
        }
    }

    private fun clearJob() {
        job?.cancel()
        job = null
    }

    private val binder = CountServiceBinder()

    override fun onBind(intent: Intent?) = binder

    inner class CountServiceBinder : Binder() {
        val service: CountService
            get() = this@CountService
    }

}