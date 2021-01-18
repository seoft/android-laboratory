package kr.co.seoft.schedule_alarm.timer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kr.co.seoft.schedule_alarm.databinding.ActivityTimerBinding
import kr.co.seoft.schedule_alarm.util.dpToPx
import kr.co.seoft.schedule_alarm.util.viewModel

class TimerActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_TIMES = "EXTRA_TIMES"

        fun startActivity(context: Context, times: IntArray?) {
            context.startActivity(Intent(context, TimerActivity::class.java).apply {
                putExtra(EXTRA_TIMES, times)
            })
        }
    }

    private lateinit var binding: ActivityTimerBinding

    private val viewModel by viewModel {
        TimerViewModel(application, intent.getIntArrayExtra(EXTRA_TIMES)?.toList() ?: emptyList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater).apply { setContentView(root) }
        binding.activity = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        if (viewModel.times.isEmpty()) finish()

        initView()
    }

    private fun initView() {
        viewModel.times.forEachIndexed { index, i ->
            val textView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                val dp10 = 10.dpToPx()
                setPadding(dp10, dp10, dp10, dp10)
                text = "${index}번째 ${i}초"
                setOnClickListener {
                    onTime(index)
                }
            }
            binding.actTimerTimes.addView(textView)
        }
    }

    private fun onTime(index: Int) {
        AlertDialog.Builder(this).apply {
            setMessage("${index}번째 ${viewModel.times.getOrNull(index) ?: return}초 부터 시작?")
            setPositiveButton("ㅇㅋ") { _, _ -> viewModel.start(index) }
        }.show()
    }

    fun onStartOrPause() {
        when (viewModel.status) {
            TimerStatus.ING -> viewModel.pause()
            TimerStatus.STOP, TimerStatus.PAUSE -> viewModel.start()
        }
    }

    fun onStopTimer() {
        viewModel.stop()
    }

}