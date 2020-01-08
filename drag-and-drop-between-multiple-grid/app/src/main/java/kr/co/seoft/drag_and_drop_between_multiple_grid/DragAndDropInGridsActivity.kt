package kr.co.seoft.drag_and_drop_between_multiple_grid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_drag_and_drop_in_grids.*
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.AppType
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.EmptyApp
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.ParentApp
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.*
import java.util.concurrent.TimeUnit
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

    private val bigRects = mutableListOf<Rect>()
    private val piecesRects = mutableListOf<Rect>()

    private val compositeDisposable = CompositeDisposable()


    lateinit var recentlyApps: MutableList<ParentApp>
    lateinit var recentlyApp: ParentApp
    var recentlyIndex = 0
    var bottomRectIndex = 0
    lateinit var savingApps: MutableList<ParentApp>

    private val centerRvAdapter by lazy {
        initFlowIconWhenInitCenterRvAdapter()
        DragAndDropInGridsRvAdapter(actDadigRvCenter.width / gridCount, clickCallback)
    }

    private val clickCallback = object : (DragAndDropInGridsRvAdapter.ClickCallbackCommand) -> Unit {
        override fun invoke(command: DragAndDropInGridsRvAdapter.ClickCallbackCommand) {

            recentlyApp = getItemFromPosition(command.position)

            when (command.type) {
                DragAndDropInGridsRvAdapter.ClickType.CLICK -> {
                    "DragAndDropInGridsRvAdapter.ClickType.CLICK".e()

                }
                DragAndDropInGridsRvAdapter.ClickType.LONG_CLICK -> {

                    "DragAndDropInGridsRvAdapter.ClickType.LONG_CLICK".e()
                    if (recentlyApp.appType == AppType.EMPTY) return
                    recentlyIndex = command.position
                    actDadigIvFloatingIcon.setImageDrawable(recentlyApp.getImage(baseContext))
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

        actDadigRvCenter.layoutManager = object : GridLayoutManager(baseContext, gridCount) {
            override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
                try {
                    super.onLayoutChildren(recycler, state)
                } catch (e: Exception) {
                    e.e()
                }
            }
        }
        actDadigRvCenter.adapter = centerRvAdapter

        centerRvAdapter.submitList(itemSets[0])
        recentlyApps = itemSets[0]

        //////
        // init bottom recycler views

        rvBottoms.forEachIndexed { index, rv ->
            rv.layoutManager = GridLayoutManager(baseContext, gridCount)
            rv.adapter = bottomRvAdapters[index]

            bottomRvAdapters[index].submitList(itemSets[index])

            rvBottoms[index].setOnTouchListener { v, event ->

                if (event.action == MotionEvent.ACTION_DOWN) {
                    centerRvAdapter.submitList(itemSets[index])
                    bottomRectIndex = index
                    recentlyApps = itemSets[index]
                }
                true
            }
        }

        compositeDisposable.add(
            Single.timer(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    initGridCoordinate()
                }, {
                    it.e()
                })
        )
    }

    private fun initGridCoordinate() {
        val centerRvPosition = IntArray(2)
        actDadigRvCenter.getLocationOnScreen(centerRvPosition)

        val left = centerRvPosition[0]
        val top = centerRvPosition[1]

        val rectSize = actDadigRvCenter.width / gridCount

        for (i in 0 until gridCount) {
            for (j in 0 until gridCount) {
                bigRects.add(
                    Rect(
                        left + rectSize * j,
                        top + rectSize * i,
                        left + rectSize * j + rectSize,
                        top + rectSize * i + rectSize
                    )
                )
            }
        }

        val piecesRectWidth = rectSize / 3

        bigRects.forEach {
            for (i in 0 until 3) {
                piecesRects.add(Rect(it.left + piecesRectWidth * i, it.top, it.left + piecesRectWidth * (i + 1), it.bottom))
            }
        }
    }

    private fun initGesture() {
        actDadigRvCenter.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent): Boolean {
                if (floatingStatus.get()) {
                    actDadigIvFloatingIcon.visibility = View.VISIBLE

                    val pointX = event.rawX.toInt() - floatingIconSize / 2
                    val pointY =
                        event.rawY.toInt() - floatingIconSize / 2 - DimensionUtil.getStatusbarHeight(baseContext) - MARGIN_TOP_ON_FINGER.dpToPx()

                    (actDadigIvFloatingIcon.layoutParams as ConstraintLayout.LayoutParams).also {
                        it.leftMargin = pointX
                        it.topMargin = pointY

                    }
                    actDadigIvFloatingIcon.requestLayout()

//                    "$pointX $pointY".e()

                    var inIndex = -1
                    piecesRects.forEachIndexed { index, rect ->
                        if (piecesRects[index].contains(event.rawX.toInt(), event.rawY.toInt())) {
                            inIndex = index
                            return@forEachIndexed
                        }
                    }
                    inRect(inIndex)

                }

                if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    floatingStatus.set(false)
                    actDadigIvFloatingIcon.visibility = View.GONE
                    centerRvAdapter.submitList(savingApps)
                    itemSets[bottomRectIndex] = savingApps
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

    }

    fun copyRecentlyApps(): MutableList<ParentApp> {
        return mutableListOf<ParentApp>().apply {
            addAll(recentlyApps)
        }
    }

    fun inRect(piecesIndex: Int) {
        actDadigTvInfo.text = ""

        // -1 : 리사이클러뷰 범위 밖 이거나
        // piecesIndex / 3 : 자기 제자리일때
        // >> 선택한자리 empty시키고 로직 진행끝
        if (piecesIndex == -1 || piecesIndex / 3 == recentlyIndex) {
            centerRvAdapter.submitList(copyRecentlyApps().apply {
                this[recentlyIndex] = EmptyApp(String.EMPTY)
            })
            savingApps = copyRecentlyApps()
            return
        }

        // 정중앙에 위치했을때 선택되어 폴더 생성 유무 선택할 수 있게 함
        if (piecesIndex % 3 == 1) {
            val pickRect = piecesIndex / 3
            actDadigTvInfo.text = "$pickRect pick"
            centerRvAdapter.submitList(copyRecentlyApps().apply {
                this[recentlyIndex] = EmptyApp(String.EMPTY)
            })

            //todo folder 생성


        } else { // 0 ~ 1/3 || 2/3 ~ 1 범위에 걸쳤을때 아이콘을 이동시킴

            // 원래 그리드 아이콘의 1/3크기로 나눠 터치범위 계산을했는데, 그 1/3나눈게 실제 그리드에서 어디속하는지 알기 위함
            val movingInRectIndex = piecesIndex / 3

            val copyApps = copyRecentlyApps().apply {
                this[recentlyIndex] = EmptyApp(String.EMPTY)
            }


            val pushIndex: Int
            var finding = 1

            // 현 손가락이 위치한 아이콘의 인덱스로 부터 좌,우로 탐색하며 가장 가까운 empty 아이콘 인덱스 파악
            while (true) {
                if (movingInRectIndex + finding < recentlyApps.size && copyApps[movingInRectIndex + finding].isEmpty()) {
                    pushIndex = finding
                    break
                } else if (movingInRectIndex - finding >= 0 && copyApps[movingInRectIndex - finding].isEmpty()) {
                    pushIndex = -finding
                    break
                }
                finding++
            }

            // 최초로 비어있는 곳 으로 부터 현 손가락이 위치한 아이콘의 인덱스 까지 하나씩 당김
            // = 현 손가락이 위치한곳에서 부터 빈곳까지 밀면서  채움
            if (pushIndex > 0) {
                for (i in 0 until pushIndex) {
                    copyApps[recentlyIndex - i] = copyApps[recentlyIndex - 1 - i]
                    copyApps[recentlyIndex - 1 - i] = EmptyApp(String.EMPTY)
                }
            } else {
                for (i in 0 until pushIndex * -1) {
                    copyApps[recentlyIndex + i] = copyApps[recentlyIndex + 1 + i]
                    copyApps[recentlyIndex + 1 + i] = EmptyApp(String.EMPTY)
                }
            }

            centerRvAdapter.submitList(copyApps)

            savingApps = mutableListOf<ParentApp>().apply {
                addAll(copyApps)
            }.apply {
                this[movingInRectIndex] = recentlyApp
            }
        }

    }


}
