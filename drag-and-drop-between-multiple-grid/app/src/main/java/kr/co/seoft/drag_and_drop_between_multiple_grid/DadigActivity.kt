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
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.AppUtil
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.DimensionUtil
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.dpToPx
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.e
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean


class DadigActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context, gridCount: Int) {

            context.startActivity(Intent(context, DadigActivity::class.java).apply {
                putExtra(EXTRA_GRID_COUNT, gridCount)
            })
        }

        private const val EXTRA_GRID_COUNT = "EXTRA_GRID_COUNT"
        private const val CENTER_RV_MARGIN = 60
        private const val FOLDER_PREVIEW_COUNT = 3
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
        DadigGridRvAdapter(actDadigRvCenter.width / gridCount, clickCallback)
    }

    private val floatingFolderRvAdapters by lazy {
        DadigGridRvAdapter(actDadigRvCenter.width / gridCount / 3)
    }

    private val clickCallback = object : (DadigGridRvAdapter.ClickCallbackCommand) -> Unit {
        override fun invoke(command: DadigGridRvAdapter.ClickCallbackCommand) {

            recentlyApp = getItemFromPosition(command.position)

            when (command.type) {
                DadigGridRvAdapter.ClickType.CLICK -> {
                    "DragAndDropInGridsRvAdapter.ClickType.CLICK".e()

                }
                DadigGridRvAdapter.ClickType.LONG_CLICK -> {

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

        actDadigRvFloatingFolder.layoutParams.apply {
            width = floatingIconSize
            height = floatingIconSize
        }

        actDadigViewFloatingFolderBg.layoutParams.apply {
            width = floatingIconSize + 6.dpToPx()
            height = floatingIconSize + 6.dpToPx()
        }
    }

    private val bottomRvAdapters by lazy {
        val bottomRvSize = actDadigRvBottom0.width

        listOf(
            DadigGridRvAdapter(bottomRvSize / gridCount),
            DadigGridRvAdapter(bottomRvSize / gridCount),
            DadigGridRvAdapter(bottomRvSize / gridCount),
            DadigGridRvAdapter(bottomRvSize / gridCount)
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

        bt.setOnClickListener {

            "recentlyApps.forEach".e()
            recentlyApps.map { if (it.label.isEmpty()) "empty" else it.label }.reduce { acc, s -> "$acc $s" }.e()

            centerRvAdapter.notifyDataSetChanged()


        }
    }

    private fun initData() {
        val apps = AppUtil.getInstalledApps(baseContext).toMutableList()
        val allGridCount = gridCount * gridCount
        for (i in 0 until 4) {
            itemSets.add(
                apps.drop(i * allGridCount).take(allGridCount).toMutableList()
            )
        }

        itemSets[0][2] = EmptyApp()
        itemSets[0][4] = EmptyApp()
        itemSets[0][7] = EmptyApp()

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
        savingApps = itemSets[0]
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


        actDadigRvFloatingFolder.layoutManager = GridLayoutManager(baseContext, 3)
        actDadigRvFloatingFolder.adapter = floatingFolderRvAdapters

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

                    (actDadigRvFloatingFolder.layoutParams as ConstraintLayout.LayoutParams).also {
                        it.leftMargin = pointX
                        it.topMargin = pointY
                    }
                    actDadigRvFloatingFolder.requestLayout()

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
                    initFloatingFolderRv()
                    centerRvAdapter.submitList(savingApps)

                    itemSets[bottomRectIndex] = savingApps
                    recentlyApps = savingApps
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

    }

    private fun copyRecentlyApps(): MutableList<ParentApp> {
        return mutableListOf<ParentApp>().apply {
            addAll(recentlyApps)
        }
    }

    fun initFloatingFolderRv() {
        floatingFolderRvAdapters.submitList(emptyList())
        floatingFolderRvAdapters.notifyDataSetChanged()
        actDadigRvFloatingFolder.visibility = View.GONE
        actDadigViewFloatingFolderBg.visibility = View.GONE
    }

    fun inRect(piecesIndex: Int) {
        actDadigTvInfo.text = ""

        // -1 : 리사이클러뷰 범위 밖 이거나
        // piecesIndex / 3 : 자기 제자리일때
        // >> 선택한자리 empty시키고 로직 진행끝
        if (piecesIndex == -1 || piecesIndex / 3 == recentlyIndex) {
            centerRvAdapter.submitList(copyRecentlyApps().apply {
                this[recentlyIndex] = EmptyApp()
            })
            savingApps = copyRecentlyApps()
            initFloatingFolderRv()
            return
        }

        // 정중앙에 위치했을때 선택되어 폴더 생성 유무 선택할 수 있게 함
        if (piecesIndex % 3 == 1) {
            val pickRect = piecesIndex / 3
            val copyApps = copyRecentlyApps()

            // 정중앙인데 Empty일경우 평소처럼 처리
            if (copyApps[pickRect].isEmpty()) {
                copyApps[recentlyIndex] = EmptyApp()

                centerRvAdapter.submitList(copyApps)

                savingApps = copyRecentlyApps().apply {
                    this[recentlyIndex] = EmptyApp()
                    this[pickRect] = recentlyApp
                }
                initFloatingFolderRv()
                return
            }

            actDadigTvInfo.text = "$pickRect pick"

            // 롱클릭할때 app과 현 rectIn app을 합쳐 folderRv를 갱신함
            floatingFolderRvAdapters.submitList(
                listOf(
                    copyApps[recentlyIndex].copy(),
                    copyApps[pickRect].copy()
                )
            )

            centerRvAdapter.submitList(copyApps.apply {
                this[recentlyIndex] = EmptyApp()
            })

            actDadigRvFloatingFolder.visibility = View.VISIBLE
            actDadigViewFloatingFolderBg.visibility = View.VISIBLE

            return

        } else { // 0 ~ 1/3 || 2/3 ~ 1 범위에 걸쳤을때 아이콘을 이동시킴

            // 원래 그리드 아이콘의 1/3크기로 나눠 터치범위 계산을했는데, 그 1/3나눈게 실제 그리드에서 어디속하는지 알기 위함
            val movingInRectIndex = piecesIndex / 3

            val copyApps = copyRecentlyApps().apply {
                this[recentlyIndex] = EmptyApp()
            }

            // 비어있는곳으로 현 손가락이 위치할 경우 아무것도 안함, 아이콘 배치 뷰 변경도 할 필요없음, 하지만 저장은 준비함
            if (copyApps[movingInRectIndex].isEmpty()) {

                centerRvAdapter.submitList(copyApps)

                val tempCopyApps = mutableListOf<ParentApp>().apply {
                    addAll(copyApps)
                }

                savingApps = tempCopyApps.apply {
                    this[movingInRectIndex] = recentlyApp
                }

                return
            }

            val emptyIndex: Int
            var finding = 1

            // 현 손가락이 위치한 아이콘의 인덱스로 부터 좌,우로 탐색하며 가장 가까운 empty 아이콘 인덱스 파악
            while (true) {
                if (movingInRectIndex + finding < recentlyApps.size && copyApps[movingInRectIndex + finding].isEmpty()) {
                    emptyIndex = movingInRectIndex + finding
                    break
                } else if (movingInRectIndex - finding >= 0 && copyApps[movingInRectIndex - finding].isEmpty()) {
                    emptyIndex = movingInRectIndex - finding
                    break
                }
                finding++
            }

            // 위에서 파악한 가장 가까운 empty 아이콘 인덱스에서 방향에 맞춰 아이콘을 하나씩 당김
            // 다 당기고 현 손가락이 위치한 뷰는 EMPTY로 하여 추후 뷰에 반영
            // 하지만 savingApps에는 현 손가락이 위치한 EMPTY 뷰 대신 recentlyApp을 넣어 저장 준비
            if (emptyIndex < movingInRectIndex) {
                for (i in 0 until finding) {
                    copyApps[emptyIndex + i] = copyApps[emptyIndex + i + 1]
                }
                copyApps[movingInRectIndex] = EmptyApp()
            } else {
                for (i in 0 until finding) {
                    copyApps[emptyIndex - i] = copyApps[emptyIndex - i - 1]
                }
                copyApps[movingInRectIndex] = EmptyApp()
            }
            centerRvAdapter.submitList(copyApps)
            savingApps = mutableListOf<ParentApp>().apply {
                addAll(copyApps)
            }.apply {
                this[movingInRectIndex] = recentlyApp
            }

            initFloatingFolderRv()
        }

    }


}