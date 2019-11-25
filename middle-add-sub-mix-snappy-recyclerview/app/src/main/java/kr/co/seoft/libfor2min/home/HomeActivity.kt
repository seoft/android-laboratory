package kr.co.seoft.libfor2min.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_home.*
import kr.co.seoft.libfor2min.R

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        init()

    }

    var k = 51

    fun init() {

        btTest1.setOnClickListener {
        }

        btTest2.setOnClickListener {
        }

        btTest3.setOnClickListener {

        }

        btTest4.setOnClickListener {
        }



        horizontalHomeList.showAddButton()
        // horizontalSnapMiddleList.hideAddButton()
        horizontalHomeList.onBadgeSelectedListener = { type, pos ->
            if(type == HomeBadgeCallbackType.ADD_PUSH)
                horizontalHomeList.addHomeBadge( HomeBadge( second = k++, type = HomeBadgeType.NORMAL ) )
        }


    }

}
