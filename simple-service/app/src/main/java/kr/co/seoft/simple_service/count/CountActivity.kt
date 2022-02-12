package kr.co.seoft.simple_service.count

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import kr.co.seoft.simple_service.databinding.ActivityCountBinding
import kr.co.seoft.simple_service.util.e

class CountActivity : AppCompatActivity() {

    private val binding by lazy { ActivityCountBinding.inflate(layoutInflater) }

    private var countService: CountService? = null
    private var connection: ServiceConnection? = null

    private var isBinding: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btStart.setOnClickListener {
            countService?.start()
        }

        binding.btPause.setOnClickListener {
            countService?.pause()
        }

        binding.btStop.setOnClickListener {
            countService?.stop()
        }

    }

    override fun onStart() {
        super.onStart()
        "onStart".e()
        bindCountService()
    }

    private fun bindCountService() {
        "bindCountService".e()
        fun setOnContListener() {
            countService?.onCountListener = object : OnCountListener {
                override fun onSecond(second: Int) {
                    binding.tvCount.text = "remain : $second"
                }

                override fun onStatus(status: CountStatus) {
                    binding.tvStatus.text = "status : ${status.name}"
                }
            }
        }

        connection = object : ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName?, service: IBinder?) {
                "onServiceConnected".e()
                val binder = service as CountService.CountServiceBinder
                countService = binder.service
                setOnContListener()
                isBinding = true
            }

            override fun onServiceDisconnected(componentName: ComponentName?) {
                "onServiceDisconnected".e()
                unbindCountService()
            }
        }
        connection?.let {
            bindService(Intent(this, CountService::class.java), it, Context.BIND_AUTO_CREATE)
        }
    }

    private fun unbindCountService() {
        "unbindCountService".e()
        countService?.onCountListener = null
        connection?.let { unbindService(it) }
    }

    override fun onStop() {
        super.onStop()
        "onStop".e()
        unbindCountService()
    }

}