package kr.co.seoft.left_side_snappy_recyclerview

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.seoft.left_side_snappy_recyclerview.util.dimen
import kr.co.seoft.left_side_snappy_recyclerview.util.i
import kr.co.seoft.left_side_snappy_recyclerview.util.pxToDp


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    lateinit var adapter: ItemRvAdapter
    lateinit var layoutManager: LinearLayoutManager

    var WIDTH = 0
    var WIDTH_HALF = 0
    var LEFT_MARGIN = 0
    var ITEM_MARGIN = 0

    var curPos = 0f
    var allPos = 0f

    var movingStart = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ITEM_MARGIN = R.dimen.itemMargin.dimen(this)
        WIDTH = R.dimen.itemWidth.dimen(this) + ITEM_MARGIN
        WIDTH_HALF = WIDTH / 2
        LEFT_MARGIN = R.dimen.rvLeftMargin.dimen(this)

        "WIDTH $WIDTH    WIDTH_HALF $WIDTH_HALF    LEFT_MARGIN $LEFT_MARGIN    ITEM_MARGIN $ITEM_MARGIN".i()


        adapter = ItemRvAdapter(this) {
            moveToIdx(it.adapterPosition - 1)
        }

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        rvContent.layoutManager = layoutManager
        rvContent.adapter = adapter



        rvContent.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)

                val position = parent.getChildAdapterPosition(view)

                if (position == 0) {
                    outRect.left = LEFT_MARGIN
                } else if (position == parent.adapter!!.itemCount - 1) {
                    outRect.left = ITEM_MARGIN
                    outRect.right = ITEM_MARGIN
                } else {
                    outRect.left = ITEM_MARGIN

                }


            }
        })


//        rvContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                dx.i()
//
//                curPos.set(curPos.get()+dx)
//            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                when (newState) {
//                    RecyclerView.SCROLL_STATE_IDLE -> {
//                        "RecyclerView.SCROLL_STATE_IDLE".i()
//                        Handler().postDelayed({rvContent.smoothScrollBy(curPos.get() * -1,0)},0)
//                    }
//                    RecyclerView.SCROLL_STATE_DRAGGING -> {
//                        "RecyclerView.SCROLL_STATE_DRAGGING".i()
//                        curPos.set(0)
//                    }
//                    RecyclerView.SCROLL_STATE_SETTLING -> {
//                        "RecyclerView.SCROLL_STATE_SETTLING".i()
//
//                    }
//
//                }
//
//            }
//
//        })

        val gestureDetector2 = GestureDetector(this, object : GestureDetector.OnGestureListener {
            override fun onShowPress(e: MotionEvent?) {
                "onShowPress ${e?.x}".i()
            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {
                "onSingleTapUp ${e?.x}".i()
                return true
            }

            override fun onDown(e: MotionEvent?): Boolean {
                "onDown ${e?.x}".i()
                return true
            }

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent?,
                velocityX: Float,
                velocityY: Float
            ): Boolean {


                return true
            }

            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {


                return true
            }

            override fun onLongPress(e: MotionEvent) {

            }

        })


        rvContent.setOnTouchListener { v, event ->

            return@setOnTouchListener gestureDetector2.onTouchEvent(event)


//            if (event.action == MotionEvent.ACTION_MOVE) {
//                if(!movingStart) {
//                    movingStart=true
//                    curPos = event.x
//                }
//            }
//
//
//
//            if (event.action == MotionEvent.ACTION_UP) {
//                movingStart = false
//                val rstPos = event.x - curPos
//
//
//                allPos += rstPos * -1
//                if (allPos < 0) allPos = 0f
//                "event.x ${event.x.toInt()}   curPos ${curPos.toInt()}  allPos ${allPos}".i()
//
////                val curPosInMethod = rvContent.computeHorizontalScrollOffset()
//                val curPosInMethod = allPos.toInt()
//                var count = -1
//
//                val toMovingPos: Int
//
//                "curPosInMethod $curPosInMethod".i()
//
//                if (curPosInMethod == 0) toMovingPos = 0
//                else {
//                    while (true) {
//                        if (WIDTH_HALF * count + WIDTH - LEFT_MARGIN < curPosInMethod &&
//                            curPosInMethod <= WIDTH_HALF * (count + 2) + WIDTH - LEFT_MARGIN
//                        ) {
//                            if (count == -1) toMovingPos = 0
//                            else toMovingPos = WIDTH_HALF * (count + 1) + WIDTH - LEFT_MARGIN
//
//                            "count $count"
//
//                            break
//                        }
//                        count += 2
//                    }
//                }
//
////                allPos = toMovingPos - curPosInMethod.toFloat()
////
////                Handler().postDelayed({
////                    rvContent.smoothScrollBy(
//////                        (toMovingPos - rvContent.computeHorizontalScrollOffset()),
////                        (toMovingPos - curPosInMethod),
////                        0
////                    )
//////                    curPos = toMovingPos
////                }, 10)
//            }


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

        bt4.setOnClickListener {
            "rvContent.computeHorizontalScrollOffset() ${rvContent.computeHorizontalScrollOffset()}  /  ${rvContent.computeHorizontalScrollOffset().pxToDp()}".i()
        }

    }

    fun moveToIdx(idx: Int) {
        val moveToPos = if (idx == 0) 0 else idx * WIDTH

        "2idx $idx     2pos $moveToPos     where ${rvContent.computeHorizontalScrollOffset()}     all ${moveToPos - rvContent.computeHorizontalScrollOffset()}".i()

        Handler().postDelayed({
            rvContent.smoothScrollBy(moveToPos - rvContent.computeHorizontalScrollOffset(), 0)
//            curPos = moveToPos
        }, 10)
    }

}

