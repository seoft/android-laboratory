package kr.co.seoft.drag_and_drop_between_multiple_grid

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_drag_and_drop_in_grids.*
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.*
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


class DadigActivity : AppCompatActivity() {

    companion object {
        fun startBasicActivity(context: Context, gridCount: Int) {

            context.startActivity(Intent(context, DadigActivity::class.java).apply {
                putExtra(EXTRA_GRID_COUNT, gridCount)
                putExtra(EXTRA_IS_BASIC, true)

            })
        }

        fun startFolderActivity(context: Context, listJson: String) {

            context.startActivity(Intent(context, DadigActivity::class.java).apply {
                putExtra(EXTRA_FOLDER_APPS, listJson)
                putExtra(EXTRA_GRID_COUNT, 3)
                putExtra(EXTRA_IS_BASIC, false)
            })
        }

        private const val EXTRA_GRID_COUNT = "EXTRA_GRID_COUNT"
        private const val EXTRA_FOLDER_APPS = "EXTRA_FOLDER_APPS"
        private const val EXTRA_IS_BASIC = "EXTRA_IS_BASIC"
        private const val CENTER_RV_MARGIN = 60
        private const val MARGIN_TOP_ON_FINGER = 20 // 손가락에 너무 겹치면 안보여서 더 잘보이게 하기 위함
        const val FOLDER_PREVIEW_COUNT = 3
        const val ICON_PADDING_RATIO = 0.1 // 그리드뷰 아이탬 패딩 주기 위함
    }

    private val gridCount by lazy { intent.getIntExtra(EXTRA_GRID_COUNT, 0) }
    private val isBasic by lazy { intent.getBooleanExtra(EXTRA_IS_BASIC, true) }
    private val appsInFolder by lazy { intent.getStringExtra(EXTRA_FOLDER_APPS) }

    private var itemSets = mutableListOf<MutableList<ParentApp>>()

    private var floatingIconSize = 0

    private var floatingStatus = AtomicInteger(FloatingStatus.NONE.id)

    private val bigRects = mutableListOf<Rect>()
    private val piecesRects = mutableListOf<Rect>()
    private val bottomRects by lazy {
        val tempList = mutableListOf<Rect>()
        rvBottoms.forEach {
            tempList.add(DimensionUtil.getViewRect(it))
        }
        tempList
    }


    private val deleteRect by lazy { DimensionUtil.getViewRect(ivDelete) }

    private val compositeDisposable = CompositeDisposable()

    private val clRoot by lazy { actDadigClRoot as ConstraintLayout }
    private val rvCenter by lazy { actDadigRvCenter as RecyclerView }
    private val ivDelete by lazy { actDadigIvDelete as ImageView }


    private var floatingView: View? = null
    private var floatingApp: ParentApp = EmptyApp()

    private lateinit var showingApps: MutableList<ParentApp>
    private var showingBottomRectIndex = 0

    private lateinit var touchUpApp: ParentApp
    private var touchUpBottomRectIndex = 0
    private var touchUpIndex = 0

    private lateinit var savingApps: MutableList<ParentApp>

    private val centerRvAdapter by lazy {
        floatingIconSize = ((rvCenter.width / gridCount) - (rvCenter.width / gridCount) * ICON_PADDING_RATIO * 2).toInt()
        DadigGridRvAdapter(rvCenter.width / gridCount, clickCallback)
    }

    private val clickCallback = object : (DadigGridRvAdapter.ClickCallbackCommand) -> Unit {
        override fun invoke(command: DadigGridRvAdapter.ClickCallbackCommand) {

            touchUpApp = getItemFromPosition(command.position)

            when (command.type) {
                DadigGridRvAdapter.ClickType.CLICK -> {
                    "DragAndDropInGridsRvAdapter.ClickType.CLICK".i()

                }
                DadigGridRvAdapter.ClickType.LONG_CLICK -> {
                    "DragAndDropInGridsRvAdapter.ClickType.LONG_CLICK".i()
                    if (touchUpApp.appType == AppType.EMPTY) return
                    ivDelete.visibility = View.VISIBLE
                    touchUpBottomRectIndex = showingBottomRectIndex
                    touchUpIndex = command.position
                    createFloatingView(touchUpApp)
                    floatingStatus.set(FloatingStatus.ING_IN.id)
                }
            }
        }
    }

    fun getItemFromPosition(position: Int): ParentApp {
        return centerRvAdapter.currentList[position]
    }

    private val bottomRvAdapters by lazy {
        val bottomRvSize = actDadigClBottom0.width

        listOf(
            DadigGridRvAdapter(bottomRvSize / gridCount),
            DadigGridRvAdapter(bottomRvSize / gridCount),
            DadigGridRvAdapter(bottomRvSize / gridCount),
            DadigGridRvAdapter(bottomRvSize / gridCount)
        )
    }

    private val rvBottoms by lazy {
        listOf(
            actDadigClBottom0,
            actDadigClBottom1,
            actDadigClBottom2,
            actDadigClBottom3
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_and_drop_in_grids)

        initData()

        clRoot.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                initView()
                initGesture()
                clRoot.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        bt.setOnClickListener {

            "recentlyApps.forEach".e()
            showingApps.map { if (it.label.isEmpty()) "empty" else it.label }.reduce { acc, s -> "$acc $s" }.e()

            centerRvAdapter.notifyDataSetChanged()
            refreshBottomRvs()


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

        if (gridCount == 3) {
            itemSets[0][2] = EmptyApp()
            itemSets[0][4] = EmptyApp()
            itemSets[0][7] = EmptyApp()

            itemSets[2].add(EmptyApp())
            itemSets[2].add(EmptyApp())

            itemSets[3].add(EmptyApp())
            itemSets[3].add(EmptyApp())
            itemSets[3].add(EmptyApp())
            itemSets[3].add(EmptyApp())
            itemSets[3].add(EmptyApp())
            itemSets[3].add(EmptyApp())
            itemSets[3].add(EmptyApp())
            itemSets[3].add(EmptyApp())
            itemSets[3].add(EmptyApp())

        } else {
            itemSets[0][2] = EmptyApp()
            itemSets[0][4] = EmptyApp()
            itemSets[0][7] = EmptyApp()

            itemSets[1].add(EmptyApp())
            itemSets[1].add(EmptyApp())
            itemSets[1].add(EmptyApp())
            itemSets[1].add(EmptyApp())
            itemSets[1].add(EmptyApp())
            itemSets[1].add(EmptyApp())
            itemSets[1].add(EmptyApp())

            repeat(16) {
                itemSets[2].add(EmptyApp())
                itemSets[3].add(EmptyApp())

            }
        }


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {

        //////
        // calc view size

        val widthAndHeight = DimensionUtil.getDeviceWidthAndHeight(this)

        rvCenter.layoutParams = (rvCenter.layoutParams as ConstraintLayout.LayoutParams).apply {
            width = widthAndHeight.first - (CENTER_RV_MARGIN * 2).dpToPx()
            height = widthAndHeight.first - (CENTER_RV_MARGIN * 2).dpToPx()
        }

        rvBottoms.forEach {
            it.layoutParams = (it.layoutParams as LinearLayout.LayoutParams).apply {
                height = actDadigClBottom0.width
            }
        }


        //////
        // init center recycler views

        rvCenter.layoutManager = GridLayoutManager(baseContext, gridCount)
        rvCenter.adapter = centerRvAdapter

        //
        if (isBasic) {
            centerRvAdapter.submitList(itemSets[0])
            showingApps = itemSets[0]
            savingApps = itemSets[0]

            showingBottomRectIndex = 0
            touchUpBottomRectIndex = 0
        } else {

        }

        //////
        // init bottom recycler views

        rvBottoms.map { it.rv }.forEachIndexed { index, rv ->
            rv.layoutManager = object : GridLayoutManager(baseContext, gridCount) {
                override fun supportsPredictiveItemAnimations(): Boolean {
                    return false
                }
            }
            rv.adapter = bottomRvAdapters[index]

            rvBottoms[index].setOnTouchListener { v, event ->

                if (event.action == MotionEvent.ACTION_DOWN) {
                    centerRvAdapter.submitList(itemSets[index])
                    showingBottomRectIndex = index
                    touchUpBottomRectIndex = index
                    showingApps = itemSets[index]
                }
                true
            }
        }

        refreshBottomRvs()

        compositeDisposable.add(
            Single.timer(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    calcViewCoordinate()
                }, {
                    it.i()
                })
        )
    }

    private fun calcViewCoordinate() {

        //////
        // rvCenter

        val rvCenterPosition = DimensionUtil.getViewPosition(rvCenter)
        val rectSize = rvCenter.width / gridCount

        for (i in 0 until gridCount) {
            for (j in 0 until gridCount) {
                bigRects.add(
                    Rect(
                        rvCenterPosition.left + rectSize * j,
                        rvCenterPosition.top + rectSize * i,
                        rvCenterPosition.left + rectSize * j + rectSize,
                        rvCenterPosition.top + rectSize * i + rectSize
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

        rvCenter.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, event: MotionEvent): Boolean {
                if (FloatingStatus.isIng(floatingStatus.get())) {
                    floatingView?.visibility = View.VISIBLE

                    val pointX = event.rawX.toInt() - floatingIconSize / 2
                    val pointY =
                        event.rawY.toInt() - floatingIconSize / 2 - DimensionUtil.getStatusbarHeight(baseContext) - MARGIN_TOP_ON_FINGER.dpToPx()

                    (floatingView?.layoutParams as ConstraintLayout.LayoutParams).also {
                        it.leftMargin = pointX
                        it.topMargin = pointY
                    }
                    floatingView?.requestLayout()

                    var inIndex = -1
                    piecesRects.forEachIndexed { index, rect ->
                        if (piecesRects[index].contains(event.rawX.toInt(), event.rawY.toInt())) {
                            inIndex = index
                            return@forEachIndexed
                        }
                    }

                    if (deleteRect.contains(event.rawX.toInt(), event.rawY.toInt())) inIndex = -2

                    inRect(inIndex)

                    bottomRects.forEachIndexed { index, rect ->
                        if (rect.contains(event.rawX.toInt(), event.rawY.toInt()) && showingBottomRectIndex != index) {

                            if (itemSets[index].none { it.isEmpty() }) {

                                actDadigTvInfo.text = "can't insert, grid is full"
                                return false
                            }

                            showingBottomRectIndex = index

                            centerRvAdapter.submitList(itemSets[index])
                            showingApps = itemSets[index]
                            savingApps = itemSets[index]
                        }
                    }

                }

                if (FloatingStatus.isIng(floatingStatus.get()) && (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL)) {

                    ivDelete.visibility = View.GONE

                    if (floatingStatus.get() == FloatingStatus.ING_REMOVE.id) {
                        floatingStatus.set(FloatingStatus.NONE.id)
                        clearFloatingView()

                        itemSets[touchUpBottomRectIndex].apply {
                            this[touchUpIndex] = EmptyApp()
                        }

                        centerRvAdapter.submitList(itemSets[touchUpBottomRectIndex])
                        showingApps = itemSets[showingBottomRectIndex]
                        refreshBottomRvs()
                        return false
                    }

                    if (floatingStatus.get() == FloatingStatus.ING_OUT.id) {
                        floatingStatus.set(FloatingStatus.NONE.id)
                        clearFloatingView()
                        centerRvAdapter.submitList(itemSets[showingBottomRectIndex])
                        return false
                    }

                    floatingStatus.set(FloatingStatus.NONE.id)

                    clearFloatingView()

                    // 다른 하단 그리드에 옮겼을때 기존 그리드 touchUp index위치 Empty 처리
                    if (!isSameTouchUpAndShowingBottomRv()) {
                        itemSets[touchUpBottomRectIndex][touchUpIndex] = EmptyApp()
                    }

                    itemSets[showingBottomRectIndex] = savingApps
                    centerRvAdapter.submitList(itemSets[showingBottomRectIndex])
                    showingApps = itemSets[showingBottomRectIndex]
                    refreshBottomRvs()

                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        })

    }

    private fun updateFloatingView(app: ParentApp) {
        if (floatingApp.hashCode() == app.hashCode()) return

        "updateFloatingView".e()

        clearFloatingView()
        floatingApp = app
        createFloatingView(app)
    }

    private fun createFloatingView(app: ParentApp) {
        floatingApp = app

        when (app.appType) {
            AppType.BASIC -> {
                floatingView = ImageView(baseContext).apply {
                    layoutParams = ViewGroup.LayoutParams(floatingIconSize, floatingIconSize)
                    visibility = View.GONE
                    id = View.generateViewId()
                }
                app.setIcon(BasicInfo(floatingView as ImageView))
                DynamicViewUtil.addViewToConstraintLayout(clRoot, floatingView as View)
            }
            AppType.FOLDER -> {
                floatingView = RecyclerView(baseContext).apply {
                    layoutParams = ViewGroup.LayoutParams(floatingIconSize, floatingIconSize)
                    visibility = View.GONE
                    id = View.generateViewId()
                }
                app.setIcon(FolderInfo(floatingView as RecyclerView, floatingIconSize / FOLDER_PREVIEW_COUNT))
                DynamicViewUtil.addViewToConstraintLayout(clRoot, floatingView as View)
            }
            else -> {
                floatingView = ImageView(baseContext).apply {
                    layoutParams = ViewGroup.LayoutParams(floatingIconSize, floatingIconSize)
                    visibility = View.GONE
                    id = View.generateViewId()
                }
                app.setIcon(EmptyInfo(floatingView as ImageView))
                DynamicViewUtil.addViewToConstraintLayout(clRoot, floatingView as View)
            }
        }
    }

    private fun clearFloatingView() {
        floatingApp = EmptyApp()
        clRoot.removeView(floatingView)
        floatingView = null
    }

    private fun refreshBottomRvs() {
        repeat(4) {
            bottomRvAdapters[it].submitList(itemSets[it].map { it.copy() }.toMutableList())
        }
    }

    private fun copyShowingApps() = showingApps.map { it.copy() }.toMutableList()
    private fun isSameTouchUpAndShowingBottomRv() = touchUpBottomRectIndex == showingBottomRectIndex
    private fun isFullFolderApp(parentApp: ParentApp) =
        parentApp.appType == AppType.FOLDER && (parentApp as FolderApp).apps.none { it.isEmpty() }

    // 아이탬 선택 후 하단 리사이클러뷰 변경여부 체크 후 변경되있다면 변경된애로 채워야함
    private fun getShowingAppBySituation() = if (isSameTouchUpAndShowingBottomRv()) EmptyApp() else showingApps[touchUpIndex]

    private fun updateSavingApps(apps: MutableList<ParentApp>) {
        if (apps.mapIndexed { index, parentApp -> parentApp.hashCode() / (index + 1) }.reduce { acc, i -> acc + i } !=
            savingApps.mapIndexed { index, parentApp -> parentApp.hashCode() / (index + 1) }.reduce { acc, i -> acc + i }) {
            savingApps = apps
            "updateSavingApps".e()
        }
    }

    fun inRect(piecesIndex: Int) {

        // delete
        if (piecesIndex == -2) {
            actDadigTvInfo.text = "delete app : ${touchUpApp.label}"
            floatingStatus.set(FloatingStatus.ING_REMOVE.id)
            return
        }

        actDadigTvInfo.text = ""

        // 원래 그리드 아이콘의 1/3크기로 나눠 터치범위 계산을했는데, 그 1/3나눈게 실제 그리드에서 어디속하는지 알기 위함
        val nextIndex = piecesIndex / 3

        // -1 : 리사이클러뷰 범위 밖 일때 >> 선택한자리 empty시키고 로직 진행끝
        if (piecesIndex == -1) {
            centerRvAdapter.submitList(copyShowingApps().apply {
                this[touchUpIndex] = getShowingAppBySituation()
            })

            floatingStatus.set(FloatingStatus.ING_OUT.id)
            updateFloatingView(touchUpApp)
            return
        }

        floatingStatus.set(FloatingStatus.ING_IN.id)

        //  자기 bottomRv이고 제 자리일때 >> 선택한자리 empty시키고 로직 진행끝
        if (isSameTouchUpAndShowingBottomRv() && nextIndex == touchUpIndex) {
            centerRvAdapter.submitList(copyShowingApps().apply {
                this[touchUpIndex] = getShowingAppBySituation()
            })

            updateSavingApps(copyShowingApps())
            updateFloatingView(touchUpApp)
            return
        }

        // 비어있는곳으로 현 손가락이 위치할 경우 아무것도 안함, 아이콘 배치 뷰 변경도 할 필요없음, 하지만 저장은 준비함
        if (showingApps[nextIndex].isEmpty()) {
            centerRvAdapter.submitList(copyShowingApps().apply { this[touchUpIndex] = getShowingAppBySituation() })
            updateFloatingView(touchUpApp)
            updateSavingApps(copyShowingApps().apply {
                this[touchUpIndex] = getShowingAppBySituation()
                this[nextIndex] = touchUpApp
            })
            return
        }

        // 정중앙에 위치했을때 선택되어 폴더 생성 유무 선택할 수 있게 함
        if (piecesIndex % 3 == 1) {
            val copyApps = copyShowingApps()

            val isFolderAndFull = isFullFolderApp(copyApps[nextIndex])

            // 폴더가 잡혔을 경우 , 새로 위치한게 폴더이지만 full일경우 => 플로팅 뷰만 기존 유지
            if (touchUpApp.appType == AppType.FOLDER || isFolderAndFull) {
                updateFloatingView(touchUpApp)
                return
            }

            actDadigTvInfo.text = "$nextIndex pick"

            // 롱클릭할때 touchUp app과 nextApp 그리고 Empty들을 3x3 or 4x4로 폴더 list로만들어 folderRv를 갱신함
            val tmpFolderApp =
                if (copyApps[nextIndex].appType != AppType.FOLDER) {
                    FolderApp(mutableListOf(touchUpApp, copyApps[nextIndex].copy()).apply {
                        repeat(FOLDER_PREVIEW_COUNT * FOLDER_PREVIEW_COUNT - 2) {
                            add(EmptyApp())
                        }
                    })
                } else { // copyApps[pickRect].appType == AppType.FOLDER
                    (copyApps[nextIndex].copy() as FolderApp).apply {
                        this.apps[apps.indexOfFirst { it.isEmpty() }] = touchUpApp
                    }
                }

            updateFloatingView(tmpFolderApp)
            tmpFolderApp.setIcon(FolderInfo(floatingView as RecyclerView, floatingIconSize / FOLDER_PREVIEW_COUNT))


            updateSavingApps(copyShowingApps().apply {
                this[touchUpIndex] = getShowingAppBySituation()
                this[nextIndex] = tmpFolderApp
            })

            centerRvAdapter.submitList(copyApps.apply {
                this[touchUpIndex] = getShowingAppBySituation()
            })

            return

        } else { // 0 ~ 1/3 || 2/3 ~ 1 범위에 걸쳤을때 아이콘을 이동시킴
            val copyApps = copyShowingApps().apply {
                this[touchUpIndex] = getShowingAppBySituation()
            }

            val emptyIndex: Int
            var finding = 1

            // 현 손가락이 위치한 아이콘의 인덱스로 부터 좌,우로 탐색하며 가장 가까운 empty 아이콘 인덱스 파악
            while (true) {
                if (nextIndex + finding < showingApps.size && copyApps[nextIndex + finding].isEmpty()) {
                    emptyIndex = nextIndex + finding
                    break
                } else if (nextIndex - finding >= 0 && copyApps[nextIndex - finding].isEmpty()) {
                    emptyIndex = nextIndex - finding
                    break
                }
                finding++
            }

            // 위에서 파악한 가장 가까운 empty 아이콘 인덱스에서 방향에 맞춰 아이콘을 하나씩 당김
            // 다 당기고 현 손가락이 위치한 뷰는 EMPTY로 하여 추후 뷰에 반영
            // 하지만 savingApps에는 현 손가락이 위치한 EMPTY 뷰 대신 recentlyApp을 넣어 저장 준비
            if (emptyIndex < nextIndex) {
                for (i in 0 until finding) {
                    copyApps[emptyIndex + i] = copyApps[emptyIndex + i + 1]
                }
                copyApps[nextIndex] = EmptyApp()
            } else {
                for (i in 0 until finding) {
                    copyApps[emptyIndex - i] = copyApps[emptyIndex - i - 1]
                }
                copyApps[nextIndex] = EmptyApp()
            }
            centerRvAdapter.submitList(copyApps)
            updateFloatingView(touchUpApp)
            updateSavingApps(mutableListOf<ParentApp>().apply {
                addAll(copyApps)
            }.apply {
                this[nextIndex] = touchUpApp
            })

        }

    }

    enum class FloatingStatus(val id: Int) {
        NONE(0),
        ING_IN(1),
        ING_OUT(2),
        ING_REMOVE(3);

        companion object {
            fun isIng(status_: Int): Boolean {
                return status_ == ING_IN.id || status_ == ING_OUT.id || status_ == ING_REMOVE.id
            }
        }
    }

}
