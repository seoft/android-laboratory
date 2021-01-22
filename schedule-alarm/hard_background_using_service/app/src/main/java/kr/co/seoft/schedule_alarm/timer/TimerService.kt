package kr.co.seoft.schedule_alarm.timer

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Binder
import android.os.IBinder
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import kr.co.seoft.schedule_alarm.util.e
import java.util.*
import java.util.concurrent.TimeUnit

class TimerService : Service() {

    lateinit var times: List<Int>

    private var remainSecondWhenPause: Long? = null
    var status = TimerStatus.STOP
        set(value) {
            field = value
            listener?.onStatusChanged(value)
        }

    var currentIndex = 0
        set(value) {
            if (field != value) listener?.onSecondAndIndexChanged(remainSecond, value)
            field = value
        }

    var remainSecond = 0L
        set(value) {
            if (field != value) {
                listener?.onSecondAndIndexChanged(value, currentIndex)
                TimerNotification.showNotification(this, times.toIntArray(), currentIndex, value)
            }
            field = value
        }

    private val mediaPlayer by lazy {
        MediaPlayer().apply {
            setDataSource(
                this@TimerService, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            )
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(USAGE_NOTIFICATION)
                .setContentType(CONTENT_TYPE_SONIFICATION)
                .build()
            setAudioAttributes(audioAttributes)
            prepare()
        }
    }

    private var endTime: Long? = null

    private var timerDisposable: Disposable? = null

    private val binder = TimerServiceBinder()

    var listener: OnTimerListener? = null

    fun setToListener(listener: OnTimerListener?) {
        this.listener = listener
    }

    fun removeListener() {
        this.listener = null
    }

    /**
     * 최초 시작 경우 times 를 통한 endTime 갱신
     * 퍼즈 후 시작 경우 remainSecondWhenPause 을 통한 endTime 갱신
     */
    fun start() {
        "start".e()
        endTime = Date().time + (remainSecondWhenPause ?: times[currentIndex] * 1000L)
        startTimerSync()
        status = TimerStatus.ING
    }

    /**
     * 인덱스 시간의 00:00부터 새로시작
     */
    fun start(index: Int) {
        "start $index".e()
        endTime = null
        stopTimerSync()

        currentIndex = index
        endTime = Date().time + times[currentIndex] * 1000L
        startTimerSync()
        status = TimerStatus.ING
    }

    fun pause() {
        "pause".e()
        stopTimerSync()
        status = TimerStatus.PAUSE
    }

    fun stop() {
        "stop".e()
        currentIndex = 0
        remainSecond = 0
        remainSecondWhenPause = null
        endTime = null
        stopTimerSync()
        status = TimerStatus.STOP

        TimerNotification.removeNotification(this)
    }

    private fun startTimerSync() {
        "startTimerSync".e()
        remainSecondWhenPause = null
        clearTimerDisposable()
        timerDisposable = Flowable.interval(0, 50, TimeUnit.MILLISECONDS).subscribe({
            endTime?.let {
                val calculatingRemainSecond = (it - Date().time)
                if (calculatingRemainSecond < 0) {
                    if (currentIndex != times.lastIndex) start(currentIndex + 1)
                    else stop()
                    ring()
                } else remainSecond = calculatingRemainSecond / 1000
            }
        }, {
            it.message?.e()
            it.printStackTrace()
        })
    }

    private fun ring() {
        mediaPlayer.start()
    }

    private fun stopTimerSync() {
        "stopTimerSync".e()
        remainSecondWhenPause = endTime?.let { it - Date().time }
        clearTimerDisposable()
        endTime = null
    }

    private fun clearTimerDisposable() {
        timerDisposable?.dispose()
        timerDisposable = null
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    inner class TimerServiceBinder : Binder() {
        val service: TimerService
            get() = this@TimerService  // return current service
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

}