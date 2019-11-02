package kr.co.seoft.left_side_snappy_recyclerview

import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.seoft.left_side_snappy_recyclerview.util.dimen
import kr.co.seoft.left_side_snappy_recyclerview.util.dpToPx
import kr.co.seoft.left_side_snappy_recyclerview.util.i


class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    var WIDTH = 0
    var WIDTH_HALF = 0
    var LEFT_MARGIN = 0
    var ITEM_MARGIN = 0
    var sttPos = 0
    var endPos = 0
    var goPos = 0

    private fun setMargins(view: View, left: Int, top: Int, right: Int, bottom: Int) {
        if (view.layoutParams is ViewGroup.MarginLayoutParams) {
            val p = view.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(left, top, right, bottom)
            view.requestLayout()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ITEM_MARGIN = R.dimen.itemMargin.dimen(this)
        WIDTH = R.dimen.itemWidth.dimen(this) + ITEM_MARGIN
        WIDTH_HALF = WIDTH / 2
        LEFT_MARGIN = R.dimen.rvLeftMargin.dimen(this)

        "WIDTH $WIDTH    WIDTH_HALF $WIDTH_HALF    LEFT_MARGIN $LEFT_MARGIN    ITEM_MARGIN $ITEM_MARGIN".i()


        llContents.setOnTouchListener { v, event ->

            if (event.action == MotionEvent.ACTION_DOWN) {
                "MotionEvent.ACTION_DOWN".i()
                sttPos = event.x.toInt() + endPos * -1

            } else if (event.action == MotionEvent.ACTION_MOVE) {
                val mv = event.x.toInt() - sttPos
                endPos = mv
                setMargins(llforSpace, mv, 0, 0, 0)
            } else if (event.action == MotionEvent.ACTION_UP) {
                "MotionEvent.ACTION_UP".i()
                if (0 * WIDTH - WIDTH_HALF < endPos && endPos <= 10 * WIDTH + WIDTH_HALF) {
                    goPos = 0 + 60.dpToPx()
                } else if (-1 * WIDTH - WIDTH_HALF < endPos && endPos <= -0 * WIDTH + WIDTH_HALF) {
                    goPos = -1 * WIDTH + 60.dpToPx()
                } else if (-2 * WIDTH - WIDTH_HALF < endPos && endPos <= -1 * WIDTH + WIDTH_HALF) {
                    goPos = -2 * WIDTH + 60.dpToPx()
                } else if (-3 * WIDTH - WIDTH_HALF < endPos && endPos <= -2 * WIDTH + WIDTH_HALF) {
                    goPos = -3 * WIDTH + 60.dpToPx()
                } else if (-4 * WIDTH - WIDTH_HALF < endPos && endPos <= -3 * WIDTH + WIDTH_HALF) {
                    goPos = -4 * WIDTH + 60.dpToPx()
                }

                moveWithAnim(endPos, goPos)
            }

            true
        }

        bt0.setOnClickListener {
            moveWithAnim(endPos, 0 * WIDTH + 60.dpToPx())
        }

        bt1.setOnClickListener {
            moveWithAnim(endPos, -1 * WIDTH + 60.dpToPx())
        }
        bt2.setOnClickListener {
            moveWithAnim(endPos, -2 * WIDTH + 60.dpToPx())
        }
        bt3.setOnClickListener {
            moveWithAnim(endPos, -3 * WIDTH + 60.dpToPx())
        }

        bt4.setOnClickListener {
            moveWithAnim(endPos, -4 * WIDTH + 60.dpToPx())
        }

    }

    fun moveWithAnim(prevPos: Int, nextPos: Int) {
        val anim = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                val params = llforSpace.layoutParams as ViewGroup.MarginLayoutParams
                params.leftMargin =
                    (nextPos + ((nextPos - prevPos) * (interpolatedTime - 1f))).toInt()
                llforSpace.layoutParams = params
            }
        }
        anim.duration = 100

        Handler().postDelayed({
            endPos = nextPos
        }, 100)
        llforSpace.startAnimation(anim)
    }

}

