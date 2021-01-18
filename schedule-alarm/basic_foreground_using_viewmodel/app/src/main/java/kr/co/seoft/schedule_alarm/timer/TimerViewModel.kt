package kr.co.seoft.schedule_alarm.timer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kr.co.seoft.schedule_alarm.util.e
import java.util.*
import java.util.concurrent.TimeUnit

class TimerViewModel(
    application: Application, val times: List<Int>
) : AndroidViewModel(application) {

    private val compositeDisposable = CompositeDisposable()

    private var timerDisposable: Disposable? = null

    private val isIng = MutableLiveData(false)
    var remainSecondWhenPause: Long? = null
    private val statusLiveData = Transformations.map(isIng) {
        when {
            !it && remainSecondWhenPause == null -> TimerStatus.STOP
            !it && remainSecondWhenPause != null -> TimerStatus.PAUSE
            else /* it == true */ -> TimerStatus.ING
        }
    }
    val status: TimerStatus
        get() = statusLiveData.value ?: TimerStatus.STOP

    val buttonStringFromStatus = Transformations.map(statusLiveData) {
        when (it) {
            TimerStatus.ING -> "일시정지"
            TimerStatus.STOP -> "시작"
            TimerStatus.PAUSE -> "다시시작"
        }
    }
    var currentIndex = 0
    var remainSecond = MutableLiveData(0L)

    var remainSecondString = Transformations.map(remainSecond) {
        "${currentIndex}번째 ${it}초 남음"
    }
    private var endTime: Long? = null

    /**
     * 최초 시작 경우 times 를 통한 endTime 갱신
     * 퍼즈 후 시작 경우 remainSecondWhenPause 을 통한 endTime 갱신
     */
    fun start() {
        "start".e()
        endTime = Date().time + (remainSecondWhenPause ?: times[currentIndex] * 1000L)
        startTimerSync()
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
    }

    fun pause() {
        "pause".e()
        stopTimerSync()
    }

    fun stop() {
        "stop".e()
        currentIndex = 0
        remainSecond.postValue(0)
        remainSecondWhenPause = null
        endTime = null
        stopTimerSync()
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
                } else remainSecond.postValue(calculatingRemainSecond / 1000)
            }
        }, {
            it.message?.e()
            it.printStackTrace()
        })
        isIng.postValue(true)
    }

    private fun stopTimerSync() {
        "stopTimerSync".e()
        remainSecondWhenPause = endTime?.let { it - Date().time }
        clearTimerDisposable()
        endTime = null
        isIng.postValue(false)
    }

    private fun clearTimerDisposable() {
        timerDisposable?.dispose()
        timerDisposable = null
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}