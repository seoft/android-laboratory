package kr.co.seoft.schedule_alarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.seoft.schedule_alarm.timer.TimerActivity
import kr.co.seoft.schedule_alarm.util.toEditable
import kr.co.seoft.schedule_alarm.util.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btStartTimerActivity.setOnClickListener {
            val times = timersEditText.text?.toString()?.split(" ")?.let {
                it.mapNotNull { it.toIntOrNull() }
            }?.toIntArray()
            if (times == null || times.isEmpty()) {
                "시간(초단위)을 입력해주세요".toast(this)
                return@setOnClickListener
            }
            TimerActivity.startActivity(this, times)
        }

        // test data
        timersEditText.text = "15 14 13".toEditable()
    }


}