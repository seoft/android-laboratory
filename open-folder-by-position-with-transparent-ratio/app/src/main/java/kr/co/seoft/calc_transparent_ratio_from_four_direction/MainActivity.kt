package kr.co.seoft.calc_transparent_ratio_from_four_direction

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.seoft.calc_transparent_ratio_from_four_direction.ProcActivity.Companion.startProcActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        go.setOnClickListener {
            startProcActivity(this, 200, 120)
        }

    }


}
