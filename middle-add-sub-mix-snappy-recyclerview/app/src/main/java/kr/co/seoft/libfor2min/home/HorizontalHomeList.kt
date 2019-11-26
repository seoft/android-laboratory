package kr.co.seoft.libfor2min.home

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.*

class HorizontalHomeList : RecyclerView {

    companion object {
        const private val LEFT_MID_POS = 3
    }

    lateinit var snapHelper: SnapHelper
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var homeBadgeAdapter: HomeBadgeAdapter
    lateinit var itemTouchHelper: ItemTouchHelper

    var onBadgeSelectedListener: ((HomeBadgeCallbackType, Int) -> Unit)? = null

    constructor(context: Context) : this(context, null) {
        initHorizontalHomeList(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        initHorizontalHomeList(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initHorizontalHomeList(context)
    }

    private fun initHorizontalHomeList(
        context: Context
    ) {
        linearLayoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
        this.layoutManager = linearLayoutManager
        homeBadgeAdapter =
            HomeBadgeAdapter(context) { type, vh ->
                val curPos = getCurPos()

                val pos = vh.adapterPosition

                // pushed Add button
                if (type == HomeBadgeCallbackType.ADD_PUSH || type == HomeBadgeCallbackType.NORMAL_PUSH) {
                    if (type == HomeBadgeCallbackType.ADD_PUSH) onBadgeSelectedListener?.invoke(type, pos)
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

            if (from.adapterPosition == 0 || to.adapterPosition == homeBadgeAdapter.itemCount
                || to.adapterPosition == 0 || from.adapterPosition == homeBadgeAdapter.itemCount
            ) {
                // 첫번째 마지막번째 버튼들있는 count에서 예외처리
                return@ItemTouchHelperCallback
            }

            homeBadgeAdapter.onItemMoved(from.adapterPosition, to.adapterPosition)
        }, {
            // call when move end after long click, for refresh badge's bottom numbers
            homeBadgeAdapter.notifyDataSetChanged()
        }))
        itemTouchHelper.attachToRecyclerView(this)

        this.smoothScrollToPosition(LEFT_MID_POS)
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
    }

    fun getCurPos() =
        (linearLayoutManager.findLastCompletelyVisibleItemPosition() + linearLayoutManager.findFirstCompletelyVisibleItemPosition()) / 2

}