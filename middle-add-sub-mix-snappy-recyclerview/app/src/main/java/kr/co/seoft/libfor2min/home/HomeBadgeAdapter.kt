package kr.co.seoft.libfor2min.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_home_badge.view.*
import kr.co.seoft.libfor2min.R

class HomeBadgeAdapter(
    val context: Context,
    val cb: (HomeBadgeCallbackType, VH) -> Unit
) : RecyclerView.Adapter<HomeBadgeAdapter.VH>() {

    private var items = mutableListOf(
        HomeBadge(0, HomeBadgeType.REPEAT_OFF),
        HomeBadge(0, HomeBadgeType.NORMAL),
        HomeBadge(0, HomeBadgeType.ADD)
    )

    /**
     * for Dispose of between empty's badge
     * default : 2
     * with add button : 3
     * 이 숫자가 [size-positionController] 에 아이템이 들어간다는거
     */
    private var positionController = 2

    /**
     * add badge
     *  - for insert between in dummy badge
     */
    fun addBadge(homeBadge: HomeBadge) {
        items.add(items.size - positionController, homeBadge)
    }

    fun getBadges() = items
    fun getBadge(pos: Int) = items[pos]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(context).inflate(R.layout.item_home_badge, parent, false), cb)
    }

    /**
     * set focus to position
     */
    fun resetFocus(position: Int) {
        removeFocus()
        items[position].type = HomeBadgeType.FOCUS
        notifyItemChanged(position)
    }

    fun onItemMoved(from: Int, to: Int) {
        if (from == to || items[to].type == HomeBadgeType.EMPTY || items[to].type == HomeBadgeType.ADD) {
            return
        }

        val fromItem = items.removeAt(from)
        items.add(to, fromItem)
        notifyItemMoved(from, to)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.setData(items[position], items.size)
    }

    /**
     * set all badge's type to normal == remove focus type
     */
    fun removeFocus() {
        for (i in 0 until items.size) {
            if (items[i].type == HomeBadgeType.FOCUS) {
                items[i].type = HomeBadgeType.NORMAL
                notifyItemChanged(i)
                return
            }
        }
    }

    inner class VH(view: View, cb: (HomeBadgeCallbackType, VH) -> Unit) :
        RecyclerView.ViewHolder(view) {

        // ref :
        // http://dudmy.net/android/2017/06/23/consider-of-recyclerview/
        // > onBindViewHolder 에 listener 성능 저하, onCreateViewHolder 혹은 RecyclerView.ViewHolder내 callback 처리

        val itemHomeBadgellContent = view.itemHomeBadgellContent
        val itemHomeBadgeIvAdd = view.itemHomeBadgeIvAdd
        val itemHomeBadgeIvRepeat = view.itemHomeBadgeIvRepeat
        val itemHomeBadgeTvTime = view.itemHomeBadgeTvTime
        val itemHomeBadgeTvCount = view.itemHomeBadgeTvCount

        init {
            itemView.setOnClickListener {

                when (items[adapterPosition].type) {
                    HomeBadgeType.NORMAL -> cb.invoke(HomeBadgeCallbackType.NORMAL_PUSH, this)
                    HomeBadgeType.ADD -> cb.invoke(HomeBadgeCallbackType.ADD_PUSH, this)
                    HomeBadgeType.REPEAT_OFF -> cb.invoke(HomeBadgeCallbackType.REPEAT_OFF_PUSH, this)
                    HomeBadgeType.REPEAT_ON -> cb.invoke(HomeBadgeCallbackType.REPEAT_ON_PUSH, this)
                }
            }

            itemView.setOnLongClickListener {
                if (items[adapterPosition].type == HomeBadgeType.NORMAL) {
                    cb.invoke(HomeBadgeCallbackType.LONG_PUSH, this)
                    removeFocus()
                }
                false
            }
        }

        fun isVisible(b: Boolean): Int {
            return if (b) View.VISIBLE
            else View.INVISIBLE
        }

        fun visibleViews(all: Boolean, content: Boolean, add: Boolean, repeat: Boolean) {
            itemView.visibility = isVisible(all)
            itemHomeBadgeIvAdd.visibility = isVisible(add)
            itemHomeBadgeIvRepeat.visibility = isVisible(repeat)
            itemHomeBadgellContent.visibility = isVisible(content)
        }

        fun setData(homeBadge: HomeBadge, wholeCount: Int) {

            /**
             * set view's style from each HomeBadgeType
             */
            if (homeBadge.type == HomeBadgeType.EMPTY) {
                itemView.visibility = View.INVISIBLE
                return
            } else {
                itemView.visibility = View.VISIBLE
            }

            when (homeBadge.type) {
                HomeBadgeType.REPEAT_ON -> {
                    visibleViews(true, false, false, true)
                }
                HomeBadgeType.REPEAT_OFF -> {
                    visibleViews(true, false, false, true)
                }
                HomeBadgeType.ADD -> {
                    visibleViews(true, false, true, false)
                }
                HomeBadgeType.EMPTY -> {
                    visibleViews(false, false, false, false)
                }
                HomeBadgeType.NORMAL -> {
                    visibleViews(true, true, false, false)
                    itemHomeBadgeTvTime.text = homeBadge.getStringUsingFormat()
                    itemHomeBadgeTvCount.text = "${adapterPosition}/${wholeCount - 2}"
                    itemHomeBadgellContent.setBackgroundResource(R.drawable.bg_timeset_times_gray_stroke)
                }
                HomeBadgeType.FOCUS -> {
                    visibleViews(true, true, false, false)
                    itemHomeBadgeTvTime.text = homeBadge.getStringUsingFormat()
                    itemHomeBadgeTvCount.text = "${adapterPosition}/${wholeCount - 2}"
                    itemHomeBadgellContent.setBackgroundResource(R.drawable.bg_timeset_times_red_stroke)
                }
            }
        }
    }
}