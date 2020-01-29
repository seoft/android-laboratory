package kr.co.seoft.drag_and_drop_between_multiple_grid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_select.view.*
import kr.co.seoft.drag_and_drop_between_multiple_grid.SelectActivity.SelectItem
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.AppUtil

class SelectRvAdapter(private val itemSize: Int, private val callback: ((SelectItem) -> Unit)? = null) :
    ListAdapter<SelectItem, SelectRvAdapter.SelectViewHolder>(
        object : DiffUtil.ItemCallback<SelectItem>() {
            override fun areItemsTheSame(oldItem: SelectItem, newItem: SelectItem): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }

            override fun areContentsTheSame(oldItem: SelectItem, newItem: SelectItem): Boolean {
                return oldItem.equals(newItem)
            }
        }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
        return SelectViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_select, parent, false),
            itemSize,
            callback
        )
    }

    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class SelectViewHolder(
        itemView: View,
        private val itemSize: Int,
        private val cb: ((SelectItem) -> Unit)? = null
    ) :
        RecyclerView.ViewHolder(itemView) {

        val ivIcon = itemView.itemSelectIvIcon
        fun bind(selectItem: SelectItem) {

            itemView.setOnClickListener {
                cb?.invoke(selectItem)
            }

            when (selectItem.type) {
                SelectActivity.SelectType.ABCD -> {
                    ivIcon.setImageResource(selectItem.resId ?: R.drawable.bg_folder_square)
                }
                SelectActivity.SelectType.SETTING -> {
                    ivIcon.setImageResource(selectItem.resId ?: R.drawable.bg_folder_square)
                }
                SelectActivity.SelectType.APP -> {
                    ivIcon.setImageDrawable(
                        AppUtil.getIconDrawableFromPkgName(itemView.context, selectItem.basicApp?.pkgName ?: "ABCD")
                    )
                }
            }

            val p7 = (itemSize * 0.7).toInt()
            val p05 = (itemSize * 0.05).toInt()
            val p10 = (itemSize * 0.10).toInt()

            ivIcon.layoutParams.height = p7
            ivIcon.layoutParams.width = p7
            (itemView.layoutParams as GridLayoutManager.LayoutParams).setMargins(p05)
            itemView.setPadding(p10)

        }
    }
}