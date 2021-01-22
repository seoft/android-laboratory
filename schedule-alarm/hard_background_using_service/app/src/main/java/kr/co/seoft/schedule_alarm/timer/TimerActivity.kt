package kr.co.seoft.schedule_alarm.timer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import kr.co.seoft.schedule_alarm.databinding.ActivityTimerBinding
import kr.co.seoft.schedule_alarm.util.dpToPx
import kr.co.seoft.schedule_alarm.util.e

class TimerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_TIMES = "EXTRA_TIMES"

        fun startActivity(context: Context, times: IntArray?) {
            context.startActivity(Intent(context, TimerActivity::class.java).apply {
                putExtra(EXTRA_TIMES, times)
            })
        }
    }

    private lateinit var binding: ActivityTimerBinding

    val times by lazy { intent.getIntArrayExtra(EXTRA_TIMES)?.toList() ?: emptyList() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater).apply { setContentView(root) }
        binding.activity = this
        binding.lifecycleOwner = this

        initView()
        initObserver()
        initService()
    }

    var isBind = false

    var timerService: TimerService? = null

    val callback = object : OnTimerListener {

        override fun onStatusChanged(status: TimerStatus) {
            buttonText.postValue(
                when (status) {
                    TimerStatus.ING -> "일시정지"
                    TimerStatus.STOP -> "시작"
                    TimerStatus.PAUSE -> "다시시작"
                }
            )
        }

        override fun onSecondAndIndexChanged(second: Long, index: Int) {
            remainTimeText.postValue("${index}번째 ${second}초 남음")
        }

    }

    private var connection: ServiceConnection? = null

    private fun initService() {

    }

    override fun onStart() {
        super.onStart()
        connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as TimerService.TimerServiceBinder
                timerService = binder.service
                timerService?.setToListener(callback)
                timerService?.times = times
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                timerService?.removeListener()
            }
        }
        connection?.let {
            bindService(Intent(this, TimerService::class.java), it, Context.BIND_AUTO_CREATE)
        }
        isBind = true
        stopService(Intent(this, TimerService::class.java))
    }

    override fun onStop() {
        super.onStop()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            startForegroundService(Intent(this, TimerService::class.java))
        } else {
            startService(Intent(this, TimerService::class.java))
        }
        connection?.let { unbindService(it) }
        isBind = false
    }


    private fun initView() {
        times.forEachIndexed { index, i ->
            val textView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                val dp10 = 10.dpToPx()
                setPadding(dp10, dp10, dp10, dp10)
                text = "${index}번째 ${i}초"
                setOnClickListener { onTime(index) }
            }
            binding.actTimerTimes.addView(textView)
        }
    }

    private fun initObserver() {

    }

    private fun onTime(index: Int) {
        AlertDialog.Builder(this).apply {
            setMessage("${index}번째 ${times.getOrNull(index) ?: return}초 부터 시작?")
            setPositiveButton("ㅇㅋ") { _, _ -> timerService?.start(index) }
        }.show()
    }

    fun onStartOrPause() {
        if (timerService?.status == TimerStatus.ING) {
            "timerService?.pause()".e()
            timerService?.pause()
        } else /* PAUSE, STOP */ {
            "timerService?.start()".e()
            timerService?.start()
        }
    }

    fun onStopTimer() {
        timerService?.stop()
    }

    /**
     * viewModel 역할 부분
     */
    val buttonText = MutableLiveData("시작")
    var remainTimeText = MutableLiveData("")

}