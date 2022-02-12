package kr.co.seoft.simple_service

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.co.seoft.simple_service.count.CountActivity
import kr.co.seoft.simple_service.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btStartCountActivity.setOnClickListener {
            startActivity(Intent(this, CountActivity::class.java))
        }
    }

}