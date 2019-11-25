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
                  val pos = vh.adapterPosition

                // pushed Add button
                if (type == HomeBadgeCallbackType.ADD_PUSH || type == HomeBadgeCallbackType.NORMAL_PUSH) {
                    if (type == HomeBadgeCallbackType.ADD_PUSH) onBadgeSelectedListener?.invoke(
                        type,
                        pos
                    )
                    homeBadgeAdapter.resetFocus(pos)

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
    }


}