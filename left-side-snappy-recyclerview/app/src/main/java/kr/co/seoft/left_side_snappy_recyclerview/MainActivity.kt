package kr.co.seoft.left_side_snappy_recyclerview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seconds = arrayListOf(
            3,
            4,
            5,
            8,
            5
        )

        lsshlv.showLeftSideSnappyHorizontalListView(seconds)

        bt0.setOnClickListener {
            lsshlv.setFocusCurrentBadge()
        }

        bt1.setOnClickListener {
            lsshlv.setFocus(0)
        }
        bt2.setOnClickListener {
            lsshlv.setFocus(1)
        }
        bt3.setOnClickListener {
            lsshlv.setFocus(3)
        }
        bt4.setOnClickListener {
            lsshlv.setFocus(4)
        }

    }
}

