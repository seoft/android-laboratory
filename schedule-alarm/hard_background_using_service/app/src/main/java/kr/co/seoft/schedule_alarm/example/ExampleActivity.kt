package kr.co.seoft.schedule_alarm.example

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import kr.co.seoft.schedule_alarm.databinding.ActivityExampleBinding
import kr.co.seoft.schedule_alarm.util.e

class ExampleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExampleBinding

    var iExampleService: IExampleService? = null
    private var connection: ServiceConnection? = null
    var isBind = false

    val callback = object : IExampleServiceCallback.Stub() {
        override fun onStringListener(s: String?) {
            "onStringListener $s".e()
            setTestText(s)
        }

        override fun onIntListener(i: Int) {
            "onIntListener $i".e()
            setTestText(i)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivityExampleBinding.inflate(layoutInflater).apply {
            binding = this
        }.root)

        binding.actExampleGetString.setOnClickListener {
            setTestText(iExampleService?.stringValue)
        }

        binding.actExampleGetInt.setOnClickListener {
            setTestText(iExampleService?.intValue)
        }

        binding.actExampleGetStringAsync.setOnClickListener {
            iExampleService?.requestStringAsync()
        }

        binding.actExampleGetIntAsync.setOnClickListener {
            iExampleService?.requestIntAsync()
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
                iExampleService = IExampleService.Stub.asInterface(service)
                iExampleService?.addCallback(callback)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                iExampleService?.removeCallback()
                iExampleService = null
            }
        }
        connection?.let {
            bindService(Intent(this, ExampleService::class.java), it, Context.BIND_AUTO_CREATE)
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