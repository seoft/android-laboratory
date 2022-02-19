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
import kr.co.seoft.simple_service.databinding.ActivityCountNotiBinding
import kr.co.seoft.simple_service.util.e

class CountNotiActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCountNotiBinding.inflate(layoutInflater) }

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
            countNotiService?.stopServiceWithActivityIfNeed()
        }

        startCountService()
        bindCountService(false)
    }

    override fun onStart() {
        super.onStart()
        "CountNotiActivity::onStart".e()
        screenAlwaysOn(true)
    }

    override fun onStop() {
        super.onStop()
        "CountNotiActivity::onStop".e()
        unbindCountService()
        screenAlwaysOn(false)
    }

    private fun startCountService() {
        "CountNotiActivity::startCountService".e()
        val intent = Intent(this, CountNotiService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) startForegroundService(intent)
        else startService(intent)
    }

    override fun onRestart() {
        super.onRestart()
        bindCountService(true)
    }

    /**
     * @param checkAliveService 엑티비티 온스탑 상태에서 백그라운드 서비스가 종료되고 온스타트되는 경우 엑티비티 종료하기 위함
     */
    private fun bindCountService(checkAliveService: Boolean) {
        "CountNotiActivity::bindCountService".e()
        if (checkAliveService && !CountNotiService.isAliveBackgroundService) finish()

        fun setOnContListener() {
            countNotiService?.onCountListener = object : OnCountListener {
                override fun onSecond(second: Int) {
                    binding.tvCount.text = "remain : $second"
                }

                override fun onStatus(status: CountStatus) {
                    binding.tvStatus.text = "status : ${status.name}"
                }

                override fun onFinish() {
                    finish()
                }
            }
        }

        connection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
                "CountNotiActivity::onServiceConnected".e()
                val binder = service as CountNotiService.CountServiceBinder
                countNotiService = binder.service
                setOnContListener()
                isBinding = true
            }

            override fun onServiceDisconnected(componentName: ComponentName?) {
                "CountNotiActivity::onServiceDisconnected".e()
                unbindCountService()
            }
        }
        connection?.let {
            bindService(Intent(this, CountNotiService::class.java), it, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindCountService() {
        "CountNotiActivity::unbindCountService".e()
        countNotiService?.onCountListener = null
        connection?.let { unbindService(it) }
        connection = null
        countNotiService = null
    }

    private fun screenAlwaysOn(remain: Boolean) {
        if (remain) window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        else window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        "CountNotiActivity::onNewIntent".e()
    }
}