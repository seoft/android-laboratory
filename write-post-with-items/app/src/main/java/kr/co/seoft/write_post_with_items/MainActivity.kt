package kr.co.seoft.write_post_with_items

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.seoft.write_post_with_items.ui.wirte.WriteActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btWrite.setOnClickListener {
            WriteActivity.startActivity(this)
        }

    }



}
