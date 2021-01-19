//package kr.co.seoft.schedule_alarm.bak
//
//import android.content.ComponentName
//import android.content.Context
//import android.content.Intent
//import android.content.ServiceConnection
//import android.os.Bundle
//import android.os.IBinder
//import android.widget.LinearLayout
//import android.widget.TextView
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.MutableLiveData
//import kr.co.seoft.schedule_alarm.ITimerService
//import kr.co.seoft.schedule_alarm.ITimerServiceCallback
//import kr.co.seoft.schedule_alarm.databinding.ActivityTimerBinding
//import kr.co.seoft.schedule_alarm.util.dpToPx
//
// 하다가 aidl 제외 구성한걸로 전환한거라
//class TimerActivity : AppCompatActivity() {
//
//    companion object {
//        private const val EXTRA_TIMES = "EXTRA_TIMES"
//
//        fun startActivity(context: Context, times: IntArray?) {
//            context.startActivity(Intent(context, TimerActivity::class.java).apply {
//                putExtra(EXTRA_TIMES, times)
//            })
//        }
//    }
//
//    private lateinit var binding: ActivityTimerBinding
//
//    val times by lazy { intent.getIntArrayExtra(EXTRA_TIMES)?.toList() ?: emptyList() }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityTimerBinding.inflate(layoutInflater).apply { setContentView(root) }
//        binding.activity = this
//        binding.lifecycleOwner = this
//
//        initView()
//
//        initService()
//    }
//
//    var isBind = false
//
//    var iTimerService: ITimerService? = null
//
//    val callback = object : ITimerServiceCallback.Stub() {
//
//        override fun onStatusChanged(status: Int) {
//            buttonText.postValue(
//                when (status) {
//                    TIMER_STATUS_ING -> "일시정지"
//                    TIMER_STATUS_STOP -> "시작"
//                    TIMER_STATUS_PAUSE -> "다시시작"
//                    else -> throw Exception("invalid status type")
//                }
//            )
//        }
//
//        override fun onSecondAndIndexChanged(second: Long, index: Int) {
//            remainTimeText.postValue("${index}번째 ${second}초 남음")
//        }
//
//    }
//
//    private var connection: ServiceConnection? = null
//
//    private fun initService() {
//
//    }
//
//    override fun onStart() {
//        super.onStart()
//        connection = object : ServiceConnection {
//            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
//                iTimerService = ITimerService.Stub.asInterface(service)
//                iTimerService?.addCallback(callback)
//            }
//
//            override fun onServiceDisconnected(name: ComponentName?) {
//                iTimerService = null
//            }
//        }
//        connection?.let {
//            bindService(Intent(this, TimerService::class.java), it, Context.BIND_AUTO_CREATE)
//
//        }
//        isBind = true
//    }
//
//    override fun onStop() {
//        super.onStop()
//        connection?.let { unbindService(it) }
//        isBind = false
//    }
//
//
//    private fun initView() {
//        times.forEachIndexed { index, i ->
//            val textView = TextView(this).apply {
//                layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
//                )
//                val dp10 = 10.dpToPx()
//                setPadding(dp10, dp10, dp10, dp10)
//                text = "${index}번째 ${i}초"
//                setOnClickListener {
//                    onTime(index)
//                }
//            }
//            binding.actTimerTimes.addView(textView)
//        }
//    }
//
//    private fun onTime(index: Int) {
//        AlertDialog.Builder(this).apply {
//            setMessage("${index}번째 ${times.getOrNull(index) ?: return}초 부터 시작?")
//            setPositiveButton("ㅇㅋ") { _, _ -> iTimerService?.startFromIndex(index) }
//        }.show()
//    }
//
//    fun onStartOrPause() {
//        if (iTimerService?.status == TIMER_STATUS_ING) {
//            iTimerService?.pause()
//        } else /* TIMER_STATUS_PAUSE, TIMER_STATUS_STOP */ {
//            iTimerService?.start()
//        }
////        when (viewModel.status) {
////            TimerStatus.ING -> viewModel.pause()
////            TimerStatus.STOP, TimerStatus.PAUSE -> viewModel.start()
////        }
//    }
//
//    fun onStopTimer() {
//        iTimerService?.stop()
////        viewModel.stop()
//    }
//
//    /**
//     * viewModel 역할 부분
//     */
//
//    val buttonText = MutableLiveData("시작")
//    var remainTimeText = MutableLiveData("")
//
//}