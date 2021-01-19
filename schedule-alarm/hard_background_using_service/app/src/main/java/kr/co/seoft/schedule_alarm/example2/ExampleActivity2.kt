package kr.co.seoft.schedule_alarm.example2

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import kr.co.seoft.schedule_alarm.databinding.ActivityExampleBinding

class ExampleActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityExampleBinding

    private var connection: ServiceConnection? = null
    var exampleService2: ExampleService2? = null
    var isBind = false

    val listener = object : OnExampleService2Listener {

        override fun onString(s: String) {
            setTestText(s)
        }

        override fun onInt(i: Int) {
            setTestText(i)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityExampleBinding.inflate(layoutInflater).apply {
            binding = this
        }.root)

        binding.actExampleGetString.setOnClickListener {
            setTestText(exampleService2?.getStringValue())
        }

        binding.actExampleGetInt.setOnClickListener {
            setTestText(exampleService2?.getIntValue())
        }

        binding.actExampleGetStringAsync.setOnClickListener {
            exampleService2?.requestStringAsync()
        }

        binding.actExampleGetIntAsync.setOnClickListener {
            exampleService2?.requestIntAsync()
        }

    }

    fun setTestText(any: Any?) {
        any ?: return
        binding.actExampleTextView.text = any.toString()
    }

    override fun onStart() {
        super.onStart()
        connection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as ExampleService2.ExampleService2Binder
                exampleService2 = binder.service
                exampleService2?.setToListener(listener)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                exampleService2?.removeListener()
                exampleService2 = null
            }
        }
        connection?.let {
            bindService(Intent(this, ExampleService2::class.java), it, Context.BIND_AUTO_CREATE)
        }
        isBind = true
    }

    override fun onStop() {
        super.onStop()
        connection?.let { unbindService(it) }
        connection = null
        isBind = false
    }

}