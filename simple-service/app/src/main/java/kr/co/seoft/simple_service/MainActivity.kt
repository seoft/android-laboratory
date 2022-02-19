package kr.co.seoft.simple_service

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.co.seoft.simple_service.count.noti.CountNotiActivity
import kr.co.seoft.simple_service.databinding.ActivityMainBinding
import kr.co.seoft.simple_service.util.e

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btStartCountNotiActivity.setOnClickListener {
            startActivity(Intent(this, CountNotiActivity::class.java))
        }

        binding.btStartCountFloatingActivity.setOnClickListener {

        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        "MainActivity::onNewIntent".e()
    }

}