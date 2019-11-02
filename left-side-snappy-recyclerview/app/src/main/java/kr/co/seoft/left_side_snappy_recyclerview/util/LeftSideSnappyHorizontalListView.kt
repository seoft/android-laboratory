package kr.co.seoft.left_side_snappy_recyclerview.util

import android.content.Context
import android.graphics.Rect
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.left_side_snappy_horizontal_list_view.view.*
import kr.co.seoft.left_side_snappy_recyclerview.R
import java.lang.Math.pow
import java.lang.Math.sqrt

open class LeftSideSnappyHorizontalListView : LinearLayout {

    companion object {
        //        private val ITEM_MARGIN = 10.dpToPx()
        private val BADGE_WIDTH = 140.dpToPx() // with margin
        private val BADGE_WIDTH_HALF = 70.dpToPx() // with margin / 2
        private val LEFT_MARGIN = 60.dpToPx()
        private val ITEM_MARGIN = 10.dpToPx()

        private val SENSITIVITY = 16.dpToPx()
    }

    private var itemCount: Int = 0
    private val items = mutableListOf<Item>()
    private lateinit var spaceLayout: LinearLayout

    var sttPos = 0
    var endPos = LEFT_MARGIN
    var rstPos = 0
    var touchDown = Pair(0F, 0F)
    var touchUp = Pair(0F, 0F)

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.left_side_snappy_horizontal_list_view, this, true)

    }

    fun showLeftSideSnappyHorizontalListView(seconds: List<Int>) {

        this.itemCount = seconds.size

        val layoutInflater = context.applicationContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        spaceLayout = spLeftMargin

        for (i in 0 until itemCount) {
            val view = layoutInflater.inflate(R.layout.item_proc_badge, null)
            val root = view.findViewById<LinearLayout>(R.id.llRoot).apply {
                layoutParams = LayoutParams(130.dpToPx(), 70.dpToPx()).apply {
                    setMargins(0, 0, 10.dpToPx(), 0)
                }
            }
            items.add(
                Item(
                    i,
                    root,
                    root.findViewById(R.id.tvCount),
                    root.findViewById(R.id.tvTime),
                    seconds[i],
                    Rect()
                )
            )
            items.last().llRoot.background = context.getDrawable(R.drawable.bg_timeset_times_gray_stroke)
            items.last().tvCount.text = "${i + 1}/${itemCount}"
            items.last().tvTime.text = seconds[i].toFormattingString()
            lsshlvRoot.addView(view)
        }

        setOnTouchListener(touchListener)

    }

    val touchListener = OnTouchListener { v, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            "MotionEvent.ACTION_DOWN".i()
            sttPos = event.x.toInt() + endPos * -1
            touchDown = Pair(event.x, event.y)
        } else if (event.action == MotionEvent.ACTION_MOVE) {
            val mv = event.x.toInt() - sttPos
            endPos = mv
            setLeftMargin(spaceLayout, mv)
        } else if (event.action == MotionEvent.ACTION_UP) {
            "MotionEvent.ACTION_UP ${endPos} ".i()
            touchUp = Pair(event.x, event.y)

            if ((sqrt(
                    pow(touchDown.first.toDouble() - touchUp.first.toDouble(), 2.toDouble())
                            + pow(touchDown.second.toDouble() - touchUp.second.toDouble(), 2.toDouble())
                )) < SENSITIVITY
            ) {
                val selectedBadge = items
                    .filter {
                        it.boundary.contains(touchUp.first.toInt(), touchUp.second.toInt())
                    }.firstOrNull()

                selectedBadge?.let {
                    moveWithAnim(
                        endPos,
                        if (it.index == 0) 0 + LEFT_MARGIN
                        else (it.index * -1) * BADGE_WIDTH + LEFT_MARGIN
                    )
                    return@OnTouchListener true
                }

            }

            if (0 * BADGE_WIDTH - ITEM_MARGIN < endPos && endPos <= Int.MAX_VALUE) {
                rstPos = 0 + LEFT_MARGIN
            } else {
                var cnt = 0
                var whileCnt = itemCount
                while (whileCnt-- > 1) {
                    if ((cnt - 1) * BADGE_WIDTH - ITEM_MARGIN < endPos && endPos <= cnt * BADGE_WIDTH - ITEM_MARGIN) {
                        rstPos = (cnt - 1) * BADGE_WIDTH + LEFT_MARGIN
                        break
                    }
                    cnt--
                }

            }

            moveWithAnim(endPos, rstPos)
        }

        true

    }

    private fun moveWithAnim(prevPos: Int, nextPos: Int) {
        "prevPos $prevPos  nextPos $nextPos".i()

        endPos = nextPos

        val anim = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                val params = spaceLayout.layoutParams as MarginLayoutParams

                params.leftMargin =
                    (nextPos + ((nextPos - prevPos) * (interpolatedTime - 1f))).toInt()
                spaceLayout.layoutParams = params
            }
        }.apply {
            duration = 200
        }

        spaceLayout.startAnimation(anim)
        Handler().postDelayed({
            endPos = nextPos


            for (i in 0 until itemCount) {
                items[i].boundary.apply {
                    left = items[i].llRoot.left
                    right = items[i].llRoot.right
                    top = items[i].llRoot.top
                    bottom = items[i].llRoot.bottom
                }
            }


        }, 200)
    }

    private fun setLeftMargin(view: View, size: Int) {
        if (view.layoutParams is MarginLayoutParams) {
            val p = view.layoutParams as MarginLayoutParams
            p.leftMargin = size
            view.requestLayout()
        }
    }

    data class Item(
        val index: Int,
        val llRoot: LinearLayout,
        val tvCount: TextView,
        val tvTime: TextView,
        val second: Int,
        var boundary: Rect
    )
}
