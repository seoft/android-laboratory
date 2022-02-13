package kr.co.seoft.floating_view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kr.co.seoft.floating_view.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btRequestOverlapPermission.setOnClickListener {

        }

        binding.btShow.setOnClickListener {

        }

        binding.btHide.setOnClickListener {

        }

    }
}