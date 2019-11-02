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
    }
}

