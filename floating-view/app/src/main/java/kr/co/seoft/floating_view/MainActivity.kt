package kr.co.seoft.floating_view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import kr.co.seoft.floating_view.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btRequestOverlapPermission.setOnClickListener {
            val intent =
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivity(intent)
        }

        binding.btShowSimple.setOnClickListener {
            startService(Intent(this, SimpleFloatingService::class.java).apply {
                putExtra(ACTION_SHOW, true)
            })
        }

        binding.btHideSimple.setOnClickListener {
            startService(Intent(this, SimpleFloatingService::class.java).apply {
                putExtra(ACTION_HIDE, true)
            })
        }

        binding.btShowComplex.setOnClickListener {
            startService(Intent(this, ComplexFloatingService::class.java).apply {
                putExtra(ACTION_SHOW, true)
            })
        }

        binding.btHideComplex.setOnClickListener {
            startService(Intent(this, ComplexFloatingService::class.java).apply {
                putExtra(ACTION_HIDE, true)
            })
        }

    }
}