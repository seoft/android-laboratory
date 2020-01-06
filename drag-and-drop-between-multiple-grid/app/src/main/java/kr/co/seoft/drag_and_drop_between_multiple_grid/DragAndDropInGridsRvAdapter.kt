package kr.co.seoft.drag_and_drop_between_multiple_grid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_application.view.*
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.AppType
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.BasicApp
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.EmptyApp
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.ParentApp
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.AppUtil

class DragAndDropInGridsRvAdapter(private val itemSize: Int) :
    ListAdapter<ParentApp, DragAndDropInGridsRvAdapter.ParentViewHolder>(
        object : DiffUtil.ItemCallback<ParentApp>() {
            override fun areItemsTheSame(oldItem: ParentApp, newItem: ParentApp): Boolean {
                return oldItem.label == newItem.label
            }

            override fun areContentsTheSame(oldItem: ParentApp, newItem: ParentApp): Boolean {
                return oldItem.label == newItem.label
            }
        }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        return when (viewType) {
            AppType.BASIC.intId -> BasicAppViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_application, parent, false)
            )
            AppType.EMPTY.intId -> EmptyAppViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false)
            )
            else -> EmptyAppViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).appType.intId
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        holder.itemView.layoutParams.height = itemSize
        holder.itemView.layoutParams.width = itemSize
        holder.itemView.setPadding(itemSize/10) // padding ratio

        when (getItem(position).appType) {
            AppType.BASIC -> (holder as BasicAppViewHolder).bind(getItem(position) as BasicApp)
            AppType.EMPTY -> (holder as EmptyAppViewHolder).bind(getItem(position) as EmptyApp)
            else -> (holder as EmptyAppViewHolder).bind(getItem(position) as EmptyApp)
        }
    }

    abstract class ParentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    class BasicAppViewHolder(itemView: View) : ParentViewHolder(itemView) {

        val ivIcon = itemView.itemApplicationIvIcon

        fun bind(basicApp: BasicApp) {
            ivIcon.setImageDrawable(AppUtil.getIconDrawableFromPkgName(itemView.context, basicApp.pkgName))
        }
    }

    class EmptyAppViewHolder(itemView: View) : ParentViewHolder(itemView) {

        val ivIcon = itemView.itemApplicationIvIcon
        fun bind(emptyApp: EmptyApp) {

        }
    }

}