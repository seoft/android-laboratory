package kr.co.seoft.drag_and_drop_between_multiple_grid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_drag_and_drop_in_grids.*
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.AppType
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.ParentApp
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.AppUtil
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.DimensionUtil
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.dpToPx
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.e
import java.util.concurrent.atomic.AtomicBoolean


class DragAndDropInGridsActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context, gridCount: Int) {

            context.startActivity(Intent(context, DragAndDropInGridsActivity::class.java).apply {
                putExtra(EXTRA_GRID_COUNT, gridCount)
            })
        }

        private const val EXTRA_GRID_COUNT = "EXTRA_GRID_COUNT"
        private const val CENTER_RV_MARGIN = 60
        private const val MARGIN_TOP_ON_FINGER = 20 // 손가락에 너무 겹치면 안보여서 더 잘보이게 하기 위함
        const val ICON_PADDING_RATIO = 0.1 // 그리드뷰 아이탬 패딩 주기 위함
    }

    private val gridCount by lazy { intent.getIntExtra(EXTRA_GRID_COUNT, 0) }

    private var itemSets = mutableListOf<MutableList<ParentApp>>()

    private var floatingIconSize = 0

    private var floatingStatus = AtomicBoolean(false)

    private val centerRvAdapter by lazy {
        initFlowIconWhenInitCenterRvAdapter()
        DragAndDropInGridsRvAdapter(actDadigRvCenter.width / gridCount, clickCallback)
    }

    private val clickCallback = object : (DragAndDropInGridsRvAdapter.ClickCallbackCommand) -> Unit {
        override fun invoke(command: DragAndDropInGridsRvAdapter.ClickCallbackCommand) {

            val app = getItemFromPosition(command.position)

            when (command.type) {
                DragAndDropInGridsRvAdapter.ClickType.CLICK -> {
                    "DragAndDropInGridsRvAdapter.ClickType.CLICK".e()

                }
                DragAndDropInGridsRvAdapter.ClickType.LONG_CLICK -> {

                    "DragAndDropInGridsRvAdapter.ClickType.LONG_CLICK".e()
                    if (app.appType == AppType.EMPTY) return
                    actDadigIvFloatingIcon.setImageDrawable(app.getImage(baseContext))
                    floatingStatus.set(true)
                }
            }
        }
    }

    fun getItemFromPosition(position: Int): ParentApp {
        return centerRvAdapter.currentList[position]
    }

    private fun initFlowIconWhenInitCenterRvAdapter() {
        val tmpFullIconSize = actDadigRvCenter.width / gridCount
        floatingIconSize = (tmpFullIconSize - tmpFullIconSize * ICON_PADDING_RATIO * 2).toInt()

        actDadigIvFloatingIcon.layoutParams.apply {
            width = floatingIconSize
            height = floatingIconSize
        }
    }

    private val bottomRvAdapters by lazy {
        val bottomRvSize = actDadigRvBottom0.width

        listOf(
            DragAndDropInGridsRvAdapter(bottomRvSize / gridCount),
            DragAndDropInGridsRvAdapter(bottomRvSize / gridCount),
            DragAndDropInGridsRvAdapter(bottomRvSize / gridCount),
            DragAndDropInGridsRvAdapter(bottomRvSize / gridCount)
        )
    }

    private val rvBottoms by lazy {
        listOf(
            actDadigRvBottom0,
            actDadigRvBottom1,
            actDadigRvBottom2,
            actDadigRvBottom3
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_and_drop_in_grids)

        initData()

        actDadigClRoot.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                initView()
                actDadigClRoot.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        initGesture()
    }

    private fun initData() {
        val apps = AppUtil.getInstalledApps(baseContext).toMutableList()
        val allGridCount = gridCount * gridCount
        for (i in 0 until 4) {
            itemSets.add(
                apps.drop(i * allGridCount).take(allGridCount).toMutableList()
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {

        //////
        // calc view size

        val widthAndHeight = DimensionUtil.getDeviceWidthAndHeight(this)

        actDadigRvCenter.layoutParams = (actDadigRvCenter.layoutParams as ConstraintLayout.LayoutParams).apply {
            width = widthAndHeight.first - (CENTER_RV_MARGIN * 2).dpToPx()
            height = widthAndHeight.first - (CENTER_RV_MARGIN * 2).dpToPx()
        }

        rvBottoms.forEach {
            it.layoutParams = (it.layoutParams as LinearLayout.LayoutParams).apply {
                height = actDadigRvBottom0.width
            }

        }

        //////
        // init center recycler views

        actDadigRvCenter.layoutManager = GridLayoutManager(baseContext, gridCount)
        actDadigRvCenter.adapter = centerRvAdapter

        centerRvAdapter.submitList(itemSets[0])

        //////
        // init bottom recycler views

        rvBottoms.forEachIndexed { index, rv ->
            rv.layoutManager = GridLayoutManager(baseContext, gridCount)
            rv.adapter = bottomRvAdapters[index]

            bottomRvAdapters[index].submitList(itemSets[index])

            rvBottoms[index].setOnTouchListener { v, event ->

                if (event.action == MotionEvent.ACTION_DOWN) centerRvAdapter.submitList(itemSets[index])
                true
            }
        }
    }

    private fun initGesture() {

        actDadigRvCenter.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent): Boolean {
                if (floatingStatus.get()) {
                    actDadigIvFloatingIcon.visibility = View.VISIBLE
                    (actDadigIvFloatingIcon.layoutParams as ConstraintLayout.LayoutParams).also {
                        it.leftMargin = event.rawX.toInt() - floatingIconSize / 2
                        it.topMargin =
                            event.rawY.toInt() - floatingIconSize / 2 - DimensionUtil.getStatusbarHeight(baseContext) - MARGIN_TOP_ON_FINGER.dpToPx()
                    }
                    actDadigIvFloatingIcon.requestLayout()
                }

                if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    floatingStatus.set(false)
                    actDadigIvFloatingIcon.visibility = View.GONE
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

    }

}
