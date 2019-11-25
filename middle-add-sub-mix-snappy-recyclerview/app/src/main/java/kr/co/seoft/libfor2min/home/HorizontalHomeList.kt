package kr.co.seoft.libfor2min.home

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.*
import kr.co.seoft.libfor2min.R

class HorizontalHomeList : RecyclerView {

    lateinit var snapHelper: SnapHelper
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var homeBadgeAdapter: HomeBadgeAdapter
    lateinit var itemTouchHelper: ItemTouchHelper

    var WHOLE_COUNT = 4

    var LEFT_REACH_CONDITION = 0
    var LEFT_MID_POS = 3

    var RIGHT_REACH_CONDITION = WHOLE_COUNT - 1
    var RIGHT_MID_POS = WHOLE_COUNT - 4

    var onBadgeSelectedListener: ((HomeBadgeCallbackType, Int) -> Unit)? = null

    constructor(context: Context) : this(context, null) {
        initHorizontalHomeList(context, true, true)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        val curAttrs = context.obtainStyledAttributes(attrs, R.styleable.HSMLAttr)
        initHorizontalHomeList(
            context,
            curAttrs.getBoolean(R.styleable.HSMLAttr_bottomNumber, true),
            curAttrs.getBoolean(R.styleable.HSMLAttr_threeDot, true)
        )
        curAttrs.recycle()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val curAttrs = context.obtainStyledAttributes(attrs, R.styleable.HSMLAttr)
        initHorizontalHomeList(
            context,
            curAttrs.getBoolean(R.styleable.HSMLAttr_bottomNumber, true),
            curAttrs.getBoolean(R.styleable.HSMLAttr_threeDot, true)
        )
        curAttrs.recycle()
    }

    private fun initHorizontalHomeList(
        context: Context,
        showBottomNumbers: Boolean,
        showThreeDots: Boolean
    ) {

        linearLayoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        this.layoutManager = linearLayoutManager
        homeBadgeAdapter =
            HomeBadgeAdapter(context, showBottomNumbers, showThreeDots) { type, vh ->
                val curPos = getCurPos()

                val pos = vh.adapterPosition

                // pushed Add button
                if (type == HomeBadgeCallbackType.ADD_PUSH || type == HomeBadgeCallbackType.NORMAL_PUSH) {
                    if (type == HomeBadgeCallbackType.ADD_PUSH) onBadgeSelectedListener?.invoke(
                        type,
                        pos
                    )
                    homeBadgeAdapter.resetFocus(pos)

                    // smoothScrollToPosition not accuracy depending on position, so add, subject value
                    if (pos >= curPos) this.smoothScrollToPosition(pos + 1)
                    else this.smoothScrollToPosition(pos - 1)

                    return@HomeBadgeAdapter
                } else if (type == HomeBadgeCallbackType.LONG_PUSH) {
                    itemTouchHelper.startDrag(vh)
                }
            }

        this.adapter = homeBadgeAdapter
        snapHelper = LinearSnapHelper()

        // ref : https://stackoverflow.com/questions/44043501/an-instance-of-onflinglistener-already-set-in-recyclerview
        this.onFlingListener = null
        snapHelper.attachToRecyclerView(this)

        itemTouchHelper = ItemTouchHelper(ItemTouchHelperCallback({ from, to ->
            homeBadgeAdapter.onItemMoved(from.adapterPosition, to.adapterPosition)
        }, {
            // call when move end after long click, for refresh badge's bottom numbers
            homeBadgeAdapter.notifyDataSetChanged()
        }))
        itemTouchHelper.attachToRecyclerView(this)

        this.smoothScrollToPosition(LEFT_MID_POS)
        this.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                // adjust position when reach first or end, cant set middle position in onScrolled callback
                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == LEFT_REACH_CONDITION)
                    this@HorizontalHomeList.smoothScrollToPosition(LEFT_MID_POS)
                else if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == RIGHT_REACH_CONDITION)
                    this@HorizontalHomeList.smoothScrollToPosition(RIGHT_MID_POS)
            }

        })
    }

    fun showAddButton() {
        homeBadgeAdapter.showAddButton()
        refreshListAndRecalc()
    }

    fun hideAddButton() {
        homeBadgeAdapter.hideAddButton()
        refreshListAndRecalc()
    }

    fun addHomeBadge(homeBadge: HomeBadge) {
        if (homeBadge.type == HomeBadgeType.ADD) return
        homeBadgeAdapter.addBadge(homeBadge)
        refreshListAndRecalc()
    }

    fun addHomeBadges(homeBadges: Array<HomeBadge>) {
        homeBadges.forEach {
            if (it.type == HomeBadgeType.NORMAL) homeBadgeAdapter.addBadge(it)
        }
        refreshListAndRecalc()
    }

    fun refreshListAndRecalc() {
        homeBadgeAdapter.notifyDataSetChanged()
        recalc()
    }

    /**
     * recalculate for set first, last badge when reach that
     */
    fun recalc() {
        WHOLE_COUNT = homeBadgeAdapter.getBadges().size
        RIGHT_REACH_CONDITION = WHOLE_COUNT - 1
        RIGHT_MID_POS = WHOLE_COUNT - 4
    }

    fun getCurPos() =
        (linearLayoutManager.findLastCompletelyVisibleItemPosition() + linearLayoutManager.findFirstCompletelyVisibleItemPosition()) / 2

}