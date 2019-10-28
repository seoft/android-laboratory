package kr.co.seoft.left_side_snappy_recyclerview

import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.seoft.left_side_snappy_recyclerview.util.dimen
import kr.co.seoft.left_side_snappy_recyclerview.util.i


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    lateinit var adapter: ItemRvAdapter
    lateinit var layoutManager: LinearLayoutManager

    var WIDTH = 0
    var WIDTH_HALF = 0

    var curPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WIDTH = (R.dimen.itemWidth.dimen(this) + R.dimen.itemMargin.dimen(this) * 2).toInt()
//            (resources.getDimension(R.dimen.itemWidth) + resources.getDimension(R.dimen.itemMargin) * 2).toInt()
        WIDTH_HALF = WIDTH / 2

        adapter = ItemRvAdapter(this) {
            moveToIdx(it.adapterPosition)
        }

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvContent.layoutManager = layoutManager
        rvContent.adapter = adapter
        rvContent.setOnTouchListener { v, event ->

            if (event.action == MotionEvent.ACTION_UP) {

                val curPosInMethod = rvContent.computeHorizontalScrollOffset()
                var count = -1

                val toMovingPos: Int

                while (true) {
                    if (WIDTH_HALF * count < curPosInMethod &&
                        curPosInMethod <= WIDTH_HALF * (count + 2)
                    ) {
                        toMovingPos = WIDTH_HALF * (count + 1)
                        break
                    }
                    count += 2
                }

                Handler().postDelayed({
                    rvContent.smoothScrollBy(
                        (toMovingPos - rvContent.computeHorizontalScrollOffset() ),
                        0
                    )
                    curPos = toMovingPos
                }, 10)
            }

            return@setOnTouchListener false

        }

        bt1.setOnClickListener {
            moveToIdx(0)
        }
        bt2.setOnClickListener {
            moveToIdx(1)
        }
        bt3.setOnClickListener {
            moveToIdx(2)
        }

    }

    fun moveToIdx(idx: Int) {
        val moveToPos = idx * WIDTH

        "moveToPos $moveToPos     curPos $curPos".i()

        Handler().postDelayed({
            rvContent.smoothScrollBy(moveToPos - curPos/* + R.dimen.rvLeftMargin.dimen(this)*/, 0)
//            curPos = moveToPos + R.dimen.rvLeftMargin.dimen(this)
            curPos = moveToPos
        }, 10)
    }

}

