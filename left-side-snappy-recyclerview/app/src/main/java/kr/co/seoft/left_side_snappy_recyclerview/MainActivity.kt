package kr.co.seoft.left_side_snappy_recyclerview

import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.seoft.left_side_snappy_recyclerview.util.dpToPx
import kr.co.seoft.left_side_snappy_recyclerview.util.i


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    lateinit var adapter: ItemRvAdapter
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ItemRvAdapter(this) {
            it.toString().i()
            "140 ${140.dpToPx()}".i()
            "it.adapterPosition ${it.adapterPosition}".i()
            rvContent.smoothScrollBy(140.dpToPx(), 0)
        }

        layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        rvContent.layoutManager = layoutManager
        rvContent.adapter = adapter

        var curPxPos = 0
        var sttPxPos = 0

        rvContent.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                /**
                 * newState
                 * 1 시작
                 * 0 떗으나 가속도붙여 진행중
                 * 2 완전멈춘상태
                 *
                 * 1 상태일떄 다음거 계산해서
                 * rvContent.computeHorizontalScrollOffset()
                 * 이거를 ㄱㄱ
                 * rvContent.smoothScrollBy(140.dpToPx(), 0)
                 *
                 */

                if (newState == 1) {
                    sttPxPos = rvContent.computeHorizontalScrollOffset()
//                    curPxPos = rvContent.computeHorizontalScrollOffset()
                    "newState == 1 ${curPxPos}".i()
                } else if (newState == 0) {

//                    val movingX = rvContent.computeHorizontalScrollOffset() - curPxPos


//                    curPxPos = rvContent.computeHorizontalScrollOffset()
//                    "newState == 1 ${movingX}".i()

//                    val half = 140.dpToPx() / 2
//
//                    val rst = when (rvContent.computeHorizontalScrollOffset()) {
//                        in 0 until half * 3 -> 0
//                        in half * 3 until half * 5 -> 1
//                        in half * 5 until half * 7 -> 2
//                        in half * 7 until half * 9 -> 3
//                        in half * 9 until half * 11 -> 4
//                        else -> 5
//                    }
//
//                    "rst $rst".i()
//                    rvContent.smoothScrollBy(rst * 140.dpToPx(), 0)
                }



                super.onScrollStateChanged(recyclerView, newState)
            }

        })

        rvContent.setOnTouchListener { v, event ->

            if (event.action == MotionEvent.ACTION_UP) {
                "b ${rvContent.computeHorizontalScrollOffset()}".i()

                // 140 367

                val half = 140.dpToPx() / 2

                val movingXPos = when (rvContent.computeHorizontalScrollOffset()) {
                    in 0 until half * 1 -> 0
                    in half * 1 until half * 3 -> half * 2
                    in half * 3 until half * 5 -> half * 4
                    in half * 5 until half * 7 -> half * 6
                    in half * 7 until half * 9 -> half * 8
                    in half * 9 until half * 11 -> half * 10
                    else -> 100
                }

                "z $movingXPos".i()
                "c ${(movingXPos - rvContent.computeHorizontalScrollOffset())}".i()
                Handler().postDelayed({
                    rvContent.smoothScrollBy(
                        (movingXPos - rvContent.computeHorizontalScrollOffset()),
                        0
                    )
                }, 100)

//                뒤에 EMPTY값 몇개 넣어주고 ㄱㄱ


//                rvContent.smoothScrollBy(movingXPos - rvContent.computeHorizontalScrollOffset(),0)
//                "movingXPos $movingXPos  -  ${movingXPos - rvContent.computeHorizontalScrollOffset()}".i()


//                "rvContent.smoothScrollBy ${rst * 140.dpToPx()}".i()
//                Handler().postDelayed({
//                    rvContent.smoothScrollBy(movingPos * -1, 0)
//                }, 0)


            }


            return@setOnTouchListener false

        }

//        val snapHelper = LinearSnapHelper()
//        snapHelper.attachToRecyclerView(rvContent)


//        rvContent.addItemDecoration(OffsetItemDecoration(this))
//        rvContent.addItemDecoration(RecyclerViewMargin(10, lm.itemCount ))


    }


}


//class OffsetItemDecoration(private val ctx: Context) : RecyclerView.ItemDecoration() {
//
//    private val screenWidth: Int
//        get() {
//
//            val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//            val display = wm.defaultDisplay
//            val size = Point()
//            display.getSize(size)
//            return size.x
//        }
//
//
//
//    // outRect의 주소를 인자로 보내면, outRect에 값들을 넣어줌.
//    // outRect의 각 자리는 각 item의 offset을 정해줘야함.
//    // default는 다 0
//    override fun getItemOffsets(
//        outRect: Rect,
//        view: View,
//        parent: RecyclerView,
//        state: RecyclerView.State
//    ) {
//
//        super.getItemOffsets(outRect, view, parent, state)
//        val offset = (screenWidth / 2.toFloat()).toInt() - view.layoutParams.width / 2
//        val lp = view.layoutParams as ViewGroup.MarginLayoutParams
//        if (parent.getChildAdapterPosition(view) == 0) {
//            (view.layoutParams as ViewGroup.MarginLayoutParams).leftMargin = 0
//            setupOutRect(outRect, offset, true)
//        } else if (parent.getChildAdapterPosition(view) == state.itemCount - 1) {
//            (view.layoutParams as ViewGroup.MarginLayoutParams).rightMargin = 0
//            setupOutRect(outRect, offset, false)
//        }
//    }
//
//    private fun setupOutRect(rect: Rect, offset: Int, start: Boolean) {
//        if (start) {
//            rect.left = offset
//        } else {
//            rect.right = offset
//        }
//    }
//}
//
//
///**
// * constructor
// * @param margin desirable margin size in px between the views in the recyclerView
// * @param columns number of columns of the RecyclerView
// */
//class RecyclerViewMargin(private val margin: Int, private val columns: Int) :
//    RecyclerView.ItemDecoration() {
//
//    /**
//     * Set different margins for the items inside the recyclerView: no top margin for the first row
//     * and no left margin for the first column.
//     */
//    override fun getItemOffsets(
//        outRect: Rect, view: View,
//        parent: RecyclerView, state: RecyclerView.State
//    ) {
//
//        val position = parent.getChildLayoutPosition(view)
//        //set right margin to all
//        outRect.right = margin
//        //set bottom margin to all
//        outRect.bottom = margin
//        //we only add top margin to the first row
//        if (position < columns) {
//            outRect.top = margin
//        }
//        //add left margin only to the first column
//        if (position % columns == 0) {
//            outRect.left = margin
//        }
//    }
//}
