package kr.co.seoft.simple_service.count.noti

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kr.co.seoft.simple_service.count.CountStatus
import kr.co.seoft.simple_service.count.OnCountListener
import kr.co.seoft.simple_service.databinding.ActivityCountBinding
import kr.co.seoft.simple_service.util.e

class CountNotiActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCountBinding.inflate(layoutInflater) }

    private var countNotiService: CountNotiService? = null
    private var connection: ServiceConnection? = null

    private var isBinding: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btStart.setOnClickListener {
            countNotiService?.start()
        }

        binding.btPause.setOnClickListener {
            countNotiService?.pause()
        }

        binding.btStop.setOnClickListener {
            finishWithStopService()
        }

        startCountService()
    }

    override fun onStart() {
        super.onStart()
        "CountActivity::onStart".e()
        bindCountService()
        screenAlwaysOn(true)
    }

    override fun onStop() {
        super.onStop()
        "CountActivity::onStop".e()
        unbindCountService()
        screenAlwaysOn(false)
    }

    private fun startCountService() {
        "CountActivity::startCountService".e()
        val intent = Intent(this, CountNotiService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent)
        else startService(intent)
    }

    private fun bindCountService() {
        "CountActivity::bindCountService".e()
        fun setOnContListener() {
            countNotiService?.onCountListener = object : OnCountListener {
                override fun onSecond(second: Int) {
                    binding.tvCount.text = "remain : $second"
                }

                override fun onStatus(status: CountStatus) {
                    if (status == CountStatus.EXIT) {
                        finishWithStopService()
                        return
                    }
                    binding.tvStatus.text = "status : ${status.name}"
                }
            }
        }

        connection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
                "CountActivity::onServiceConnected".e()
                val binder = service as CountNotiService.CountServiceBinder
                countNotiService = binder.service
                setOnContListener()
                isBinding = true
            }

            override fun onServiceDisconnected(componentName: ComponentName?) {
                "CountActivity::onServiceDisconnected".e()
                unbindCountService()
            }
        }
        connection?.let {
            bindService(Intent(this, CountNotiService::class.java), it, Context.BIND_AUTO_CREATE)
        }
    }

    private fun finishWithStopService() {
        countNotiService?.stopService()
        finish()
    }

    private fun unbindCountService() {
        "CountActivity::unbindCountService".e()
        countNotiService?.onCountListener = null
        connection?.let { unbindService(it) }
        connection = null
        countNotiService = null
    }

    private fun screenAlwaysOn(remain: Boolean) {
        if (remain) window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        else window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}