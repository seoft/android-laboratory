package kr.co.seoft.drag_and_drop_between_multiple_grid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.ParentApp
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.AppUtil
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.Preferencer

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start33.setOnClickListener {
            DadigActivity.startBasicActivity(this)
        }

        start44.setOnClickListener {

        }

        resetItems33.setOnClickListener {
            initData(3)
        }

        resetItems44.setOnClickListener {

        }


    }


    fun initData(count: Int) {
        val apps = AppUtil.getInstalledApps(baseContext).toMutableList()
        val allGridCount = count * count

        val infiniteApps = arrayListOf<ParentApp>(
            *apps.toTypedArray(),
            *apps.toTypedArray(),
            *apps.toTypedArray(),
            *apps.toTypedArray(),
            *apps.toTypedArray(),
            *apps.toTypedArray()
        )


        Preferencer.setAllApps(
            this, listOf(
                listOf(*infiniteApps.drop(allGridCount * 0).take(allGridCount * 1).toTypedArray()),
                listOf(*infiniteApps.drop(allGridCount * 1).take(allGridCount * 2).toTypedArray()),
                listOf(*infiniteApps.drop(allGridCount * 2).take(allGridCount * 3).toTypedArray()),
                listOf(*infiniteApps.drop(allGridCount * 3).take(allGridCount * 4).toTypedArray())
            )
        )


    }

}
