package kr.co.seoft.calc_transparent_ratio_from_four_direction

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_proc.*
import kr.co.seoft.calc_transparent_ratio_from_four_direction.util.DimensionUtil
import kr.co.seoft.calc_transparent_ratio_from_four_direction.util.dpToPx
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt


class ProcActivity : AppCompatActivity() {


    companion object {
        fun startProcActivity(context: Context, centerSize: Int, distance: Int) {
            context.startActivity(Intent(context, ProcActivity::class.java).apply {
                putExtra(EXTRA_CENTER_SIZE, centerSize)
                putExtra(EXTRA_DISTANCE, distance)
            })
        }

        private const val EXTRA_CENTER_SIZE = "EXTRA_CENTER_SIZE"
        private const val EXTRA_DISTANCE = "EXTRA_DISTANCE"

        private const val LEFT = 0
        private const val TOP = 1
        private const val RIGHT = 2
        private const val BOTTOM = 3

        private const val MIN_ALPHA = 0.2f
        private const val MAX_ALPHA = 1f

        private const val ANIM_DURATION = 500L
    }

    private val PREVIEW_SIZE = 50.dpToPx()
    private val PREVIEW_SIZE_HALF = PREVIEW_SIZE / 2
    private val DISTANCE_OF_SHOW_PREVIEW = 30.dpToPx()

    private val centerSize by lazy { intent.getIntExtra(EXTRA_CENTER_SIZE, 0).dpToPx() }
    private val distance by lazy { intent.getIntExtra(EXTRA_DISTANCE, 0).dpToPx() }
    private val bottomNavigationbarHeight by lazy { DimensionUtil.getBottomNavigationbarHeight(this) }
    private var startPoint = Point()
    private val openPoints = Array(4) { Point() }
    private var status = Status.NONE

    private var itemSets = mutableListOf<List<Int>>()

    private var openIndex = -1
    private var selectIndex = -1


    private val rects = Array(9) { Rect() }

    private val rvPreviews by lazy {
        listOf(actProcRvPreview1, actProcRvPreview2, actProcRvPreview3, actProcRvPreview4)
    }

    private val previewAdapters by lazy {
        List(4) { GridAdapter(PREVIEW_SIZE / 3) }
    }

    private val centerAdapter by lazy {
        GridAdapter(centerSize / 3) {
            Toast.makeText(
                this,
                "$openIndex $it",
                Toast.LENGTH_SHORT
            ).show()
            setStatusOpenToNone()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proc)

        initData()
        initView()

        Handler().postDelayed({
            setTouchLogic()
        }, 300L)

    }

    private fun initData() {
        val ran = Random(1234)
        repeat(4) {
            itemSets.add(
                List(9) {
                    Color.argb(255, ran.nextInt(256), ran.nextInt(256), ran.nextInt(256))
                })
        }
    }

    private fun initView() {
        actProcRvCenter.layoutManager = GridLayoutManager(this, 3)
        actProcRvCenter.adapter = centerAdapter
        actProcRvCenter.layoutParams.width = centerSize
        actProcRvCenter.layoutParams.height = centerSize
        actProcRvCenter.itemAnimator = null

        repeat(4) {
            rvPreviews[it].layoutManager = GridLayoutManager(this, 3)
            rvPreviews[it].adapter = previewAdapters[it]
            rvPreviews[it].layoutParams.width = PREVIEW_SIZE
            rvPreviews[it].layoutParams.height = PREVIEW_SIZE
            rvPreviews[it].itemAnimator = null
            previewAdapters[it].submitList(itemSets[it])
        }

    }

    private fun setTouchLogic() {

        val rootRect = DimensionUtil.getViewRect(actProcClRoot)

        actProcClRoot.setOnTouchListener { v, event ->
            var x = event.x.toInt()
            var y = event.y.toInt()

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {

                    if (status.isOpen()) {
                        actProcRvCenter.visibility = View.INVISIBLE
                    }

                    if (x - distance - PREVIEW_SIZE_HALF < 0) {
                        x = distance + PREVIEW_SIZE_HALF
                    } else if (x + distance + PREVIEW_SIZE_HALF > rootRect.right) {
                        x = rootRect.right - distance - PREVIEW_SIZE_HALF
                    }

                    if (y - distance - PREVIEW_SIZE_HALF < 0) {
                        y = distance + PREVIEW_SIZE_HALF
                    } else if (y + distance > rootRect.bottom - bottomNavigationbarHeight) {
                        y = rootRect.bottom - distance - bottomNavigationbarHeight
                    }

                    startPoint.set(x, y)

                    (actProcIvTouch.layoutParams as ConstraintLayout.LayoutParams).apply {
                        this.leftMargin = x
                        this.topMargin = y
                    }
                    actProcIvTouch.requestLayout()

                    openPoints[LEFT].set(x - distance, y)
                    openPoints[TOP].set(x, y - distance)
                    openPoints[RIGHT].set(x + distance, y)
                    openPoints[BOTTOM].set(x, y + distance)

                    repeat(4) {
                        (rvPreviews[it].layoutParams as ConstraintLayout.LayoutParams).leftMargin =
                            openPoints[it].x - PREVIEW_SIZE_HALF
                        (rvPreviews[it].layoutParams as ConstraintLayout.LayoutParams).topMargin =
                            openPoints[it].y - PREVIEW_SIZE_HALF
                        rvPreviews[it].alpha = MIN_ALPHA
                    }

                    status = Status.EMPTY
                }
                MotionEvent.ACTION_MOVE -> {
                    if (status.isEmpty()) {

                        openIndex = -1

                        if (rvPreviews[0].visibility != View.VISIBLE
                            && getInterval(startPoint, Point(x, y)) > DISTANCE_OF_SHOW_PREVIEW
                        ) {
                            repeat(4) {
                                rvPreviews[it].visibility = View.VISIBLE
                            }
                        }

                        repeat(4) {

                            var interval = getInterval(openPoints[it], Point(x, y))
                            if (interval > distance) interval = distance.toFloat()

                            rvPreviews[it].alpha =
                                (((distance - interval)) / distance * (MAX_ALPHA - MIN_ALPHA) + MIN_ALPHA)


                            if (interval < PREVIEW_SIZE_HALF) {
                                openIndex = it
                                return@repeat
                            }
                        }

                        if (openIndex != -1) {
                            repeat(4) { idx2 ->
                                rvPreviews[idx2].alpha = MIN_ALPHA
                                rvPreviews[idx2].visibility = View.INVISIBLE
                            }

                            (actProcRvCenter.layoutParams as ConstraintLayout.LayoutParams).topMargin =
                                startPoint.y - centerSize / 2
                            (actProcRvCenter.layoutParams as ConstraintLayout.LayoutParams).leftMargin =
                                startPoint.x - centerSize / 2
                            actProcRvCenter.visibility = View.VISIBLE
                            actProcRvCenter.requestLayout()

                            val rvCenterPosition = DimensionUtil.LeftAndTop(
                                startPoint.x - centerSize / 2,
                                startPoint.y - centerSize / 2
                            )

                            val tmpSize = centerSize / 3

                            var rectIndex = 0
                            repeat(3) { i ->
                                repeat(3) { j ->
                                    rects[rectIndex++].apply {
                                        left = rvCenterPosition.left + tmpSize * j
                                        top = rvCenterPosition.top + tmpSize * i
                                        right = rvCenterPosition.left + tmpSize * j + tmpSize
                                        bottom = rvCenterPosition.top + tmpSize * i + tmpSize
                                    }
                                }
                            }
                            actProcRvCenter.animate().alpha(0f).setDuration(0L).start()
                            actProcRvCenter.animate().alpha(1.0f).setDuration(ANIM_DURATION).start()

                            centerAdapter.submitList(itemSets[openIndex])
                            status = Status.OPEN
                        }

                    } else if (status.isOpen() && openIndex != -1) {

                        actProcViewTopPreview.visibility

                        selectIndex = -1

                        rects.forEachIndexed { index, rect ->
                            if (rect.contains(x, y)) {
                                selectIndex = index
                                return@forEachIndexed
                            }
                        }

                        if (selectIndex != -1) {
                            actProcViewTopPreview.setBackgroundColor(itemSets[openIndex][selectIndex])
                            actProcViewTopPreview.visibility = View.VISIBLE
                        } else {
                            actProcViewTopPreview.visibility = View.INVISIBLE
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {

                    if (status.isOpen()) {
                        if (selectIndex == -1) {


                        } else {
                            Toast.makeText(
                                this,
                                "$openIndex $selectIndex",
                                Toast.LENGTH_SHORT
                            ).show()
                            setStatusOpenToNone()
                        }

                    } else {
                        // folder 처리
                    }

                    actProcViewTopPreview.visibility = View.INVISIBLE

                    repeat(4) {
                        rvPreviews[it].visibility = View.INVISIBLE
                    }
                }

            }

            true
        }

    }

    private fun setStatusOpenToNone() {
        actProcRvCenter.visibility = View.INVISIBLE
        status = Status.NONE
    }

    fun getInterval(p1: Point, p2: Point): Float {

        return sqrt(
            (p1.x - p2.x).toDouble().pow(2) +
                    (p1.y - p2.y).toDouble().pow(2)
        ).toFloat()
    }

    enum class Status {
        NONE,
        EMPTY,
        OPEN,
        OPEN_FOLDER;

        fun isEmpty() = this == EMPTY
        fun isOpen() = this == OPEN
        fun isOpenFolder() = this == OPEN_FOLDER

    }

}
