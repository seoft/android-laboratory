package kr.co.seoft.schedule_alarm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.seoft.schedule_alarm.example.ExampleActivity
import kr.co.seoft.schedule_alarm.example2.ExampleActivity2
import kr.co.seoft.schedule_alarm.timer.TimerActivity
import kr.co.seoft.schedule_alarm.util.toEditable
import kr.co.seoft.schedule_alarm.util.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btServiceWithAidlExample.setOnClickListener {
            startActivity(Intent(this, ExampleActivity::class.java))
        }

        btServiceWithoutAidlExample.setOnClickListener {
            startActivity(Intent(this, ExampleActivity2::class.java))
        }

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
        timersEditText.text = "5 4 3".toEditable()
//        timersEditText.text = "1 2".toEditable()
    }


}