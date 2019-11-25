package kr.co.seoft.libfor2min.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_home_badge.view.*
import kr.co.seoft.libfor2min.R

class HomeBadgeAdapter(
    val context: Context,
    val showBottomNumbers: Boolean,
    val showThreeDots: Boolean,
    val cb: (HomeBadgeCallbackType, VH) -> Unit
) : RecyclerView.Adapter<HomeBadgeAdapter.VH>() {

    private var items = mutableListOf<HomeBadge>()

    /**
     * add badge
     *  - for insert between in dummy badge
     */
    fun addBadge(homeBadge: HomeBadge) {
        if (items.size == 0)
            items.add(0, homeBadge)
        else
            items.add(items.size - 1, homeBadge)
    }

    fun getBadges() = items
    fun getBadge(pos: Int) = items[pos]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(context).inflate(R.layout.item_home_badge, parent, false), cb)
    }

    fun hasAddButton() = items.any { it.type == HomeBadgeType.ADD }

    fun showAddButton() {
        if (hasAddButton()) return
        addBadge(HomeBadge(-1, 0, HomeBadgeType.ADD))
    }

    fun hideAddButton() {
        if (!hasAddButton()) return
        items.removeAt(items.size - 1)
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

        /**
         * set view's style from each HomeBadgeType
         */
        if (items[position].type == HomeBadgeType.EMPTY) {
            holder.itemView.visibility = View.INVISIBLE
        } else if (items[position].type == HomeBadgeType.ADD) {
            holder.itemView.visibility = View.VISIBLE
            holder.tvAdd.visibility = View.VISIBLE
            holder.tvName.visibility = View.GONE
            holder.tvThreeDot.visibility = View.INVISIBLE
            holder.tvBottomNumber.visibility = View.INVISIBLE
        } else if (items[position].type == HomeBadgeType.NORMAL) {
            holder.itemView.visibility = View.VISIBLE
            holder.tvName.text = items[position].getStringUsingFormat()
            holder.tvName.setTextColor(Color.BLACK)
            holder.tvName.setBackgroundResource(0)
            holder.tvName.visibility = View.VISIBLE
            holder.tvAdd.visibility = View.GONE
            holder.tvThreeDot.visibility = View.INVISIBLE
            if (showBottomNumbers) {
                holder.tvBottomNumber.visibility = View.VISIBLE
                holder.tvBottomNumber.text = "${position - 1}"
            }
        } else if (items[position].type == HomeBadgeType.FOCUS) {
            holder.itemView.visibility = View.VISIBLE
            holder.tvName.text = items[position].getStringUsingFormat()
            holder.tvName.setTextColor(Color.WHITE)
            holder.tvName.setBackgroundResource(R.drawable.bg_selected_badge)
            holder.tvName.visibility = View.VISIBLE
            holder.tvAdd.visibility = View.GONE
            if (showThreeDots) holder.tvThreeDot.visibility = View.VISIBLE
            if (showBottomNumbers) {
                holder.tvBottomNumber.visibility = View.VISIBLE
                holder.tvBottomNumber.text = "${position - 1}"
            }
        }
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

        init {
            itemView.setOnClickListener {
                if (items[adapterPosition].type == HomeBadgeType.NORMAL)
                    cb.invoke(HomeBadgeCallbackType.NORMAL_PUSH, this)
                else if (items[adapterPosition].type == HomeBadgeType.ADD)
                    cb.invoke(HomeBadgeCallbackType.ADD_PUSH, this)
            }

            itemView.setOnLongClickListener {
                if (items[adapterPosition].type == HomeBadgeType.NORMAL) {
                    cb.invoke(HomeBadgeCallbackType.LONG_PUSH, this)
                    removeFocus()
                }
                true
            }
        }

        val tvThreeDot = view.tvThreeDot
        val tvName = view.tvName
        val tvAdd = view.tvAdd
        val tvBottomNumber = view.tvBottomNumber
    }


}