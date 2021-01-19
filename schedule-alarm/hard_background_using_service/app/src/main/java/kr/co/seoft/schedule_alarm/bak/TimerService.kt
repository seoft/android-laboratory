//package kr.co.seoft.schedule_alarm.bak
//
//import android.app.Service
//import android.content.Intent
//import android.os.IBinder
//import io.reactivex.Flowable
//import io.reactivex.disposables.Disposable
//import kr.co.seoft.schedule_alarm.ITimerService
//import kr.co.seoft.schedule_alarm.ITimerServiceCallback
//import kr.co.seoft.schedule_alarm.util.e
//import java.util.*
//import java.util.concurrent.TimeUnit
//
//class TimerService : Service() {
//
//    lateinit var times: List<Int>
//    private var remainSecondWhenPause: Long? = null
//    var status = TIMER_STATUS_STOP
//        set(value) {
//            field = value
//            iTimerServiceCallback?.onStatusChanged(value)
//        }
//
//    var currentIndex = 0
//        set(value) {
//            field = value
//            iTimerServiceCallback?.onSecondAndIndexChanged(remainSecond, value)
//        }
//    var remainSecond = 0L
//        set(value) {
//            field = value
//            iTimerServiceCallback?.onSecondAndIndexChanged(value, currentIndex)
//        }
//    private var endTime: Long? = null
//
//    private var timerDisposable: Disposable? = null
//
//    var iTimerServiceCallback: ITimerServiceCallback? = null
//
//    /**
//     * 최초 시작 경우 times 를 통한 endTime 갱신
//     * 퍼즈 후 시작 경우 remainSecondWhenPause 을 통한 endTime 갱신
//     */
//    fun start() {
//        "start".e()
//        endTime = Date().time + (remainSecondWhenPause ?: times[currentIndex] * 1000L)
//        startTimerSync()
//    }
//
//    /**
//     * 인덱스 시간의 00:00부터 새로시작
//     */
//    fun start(index: Int) {
//        "start $index".e()
//        endTime = null
//        stopTimerSync()
//
//        currentIndex = index
//        endTime = Date().time + times[currentIndex] * 1000L
//        startTimerSync()
//        status = TIMER_STATUS_ING
//    }
//
//    fun pause() {
//        "pause".e()
//        stopTimerSync()
//        status = TIMER_STATUS_PAUSE
//    }
//
//    fun stop() {
//        "stop".e()
//        currentIndex = 0
//        remainSecond = 0
//        remainSecondWhenPause = null
//        endTime = null
//        stopTimerSync()
//        status = TIMER_STATUS_STOP
//    }
//
//    private fun startTimerSync() {
//        "startTimerSync".e()
//        remainSecondWhenPause = null
//        clearTimerDisposable()
//        timerDisposable = Flowable.interval(0, 50, TimeUnit.MILLISECONDS).subscribe({
//            endTime?.let {
//                val calculatingRemainSecond = (it - Date().time)
//                if (calculatingRemainSecond < 0) {
//                    if (currentIndex != times.lastIndex) start(currentIndex + 1)
//                    else stop()
//                } else remainSecond = calculatingRemainSecond / 1000
//            }
//        }, {
//            it.message?.e()
//            it.printStackTrace()
//        })
//    }
//
//    private fun stopTimerSync() {
//        "stopTimerSync".e()
//        remainSecondWhenPause = endTime?.let { it - Date().time }
//        clearTimerDisposable()
//        endTime = null
//    }
//
//    private fun clearTimerDisposable() {
//        timerDisposable?.dispose()
//        timerDisposable = null
//    }
//
//    private val binder = object : ITimerService.Stub() {
//
//        override fun setTimes(times: IntArray?) {
//            this@TimerService.times = times?.toList() ?: emptyList()
//        }
//
//        override fun getStatus(): Int {
//            return this@TimerService.status
//        }
//
//        override fun getCurrentIndex(): Int {
//            return this@TimerService.currentIndex
//        }
//
//        override fun getRemainSecond(): Long {
//            return this@TimerService.remainSecond
//        }
//
//        override fun start() {
//            this@TimerService.start()
//        }
//
//        override fun startFromIndex(index: Int) {
//            this@TimerService.start(index)
//        }
//
//        override fun pause() {
//            this@TimerService.pause()
//        }
//
//        override fun stop() {
//            this@TimerService.stop()
//        }
//
//        override fun addCallback(callback: ITimerServiceCallback?) {
//            iTimerServiceCallback = callback
//        }
//
//        override fun removeCallback() {
//            iTimerServiceCallback = null
//        }
//    }
//
//
//    override fun onBind(intent: Intent?): IBinder? {
//        return binder
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//    }
//
//}