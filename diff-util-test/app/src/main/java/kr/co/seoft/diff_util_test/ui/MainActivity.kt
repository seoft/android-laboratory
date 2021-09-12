package kr.co.seoft.diff_util_test.ui

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kr.co.seoft.diff_util_test.R
import kr.co.seoft.diff_util_test.ui.test1.Test1Activity
import kr.co.seoft.diff_util_test.ui.test2.Test2Activity
import kr.co.seoft.diff_util_test.ui.test3.Test3Activity

class MainActivity : AppCompatActivity(R.layout.activity_main) {


    fun btTest1(view: View) {
        startActivity(Intent(this, Test1Activity::class.java))
    }

    fun btTest2(view: View) {
        startActivity(Intent(this, Test2Activity::class.java))
    }

    fun btTest3(view: View) {
        startActivity(Intent(this, Test3Activity::class.java))
    }


}