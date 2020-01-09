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

class DadigGridRvAdapter(
    private val itemSize: Int,
    private val callback: ((ClickCallbackCommand) -> Unit)? = null
) : ListAdapter<ParentApp, DadigGridRvAdapter.ParentViewHolder>(
    object : DiffUtil.ItemCallback<ParentApp>() {
        override fun areItemsTheSame(oldItem: ParentApp, newItem: ParentApp): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: ParentApp, newItem: ParentApp): Boolean {
            return oldItem.label == newItem.label && oldItem.appType == newItem.appType
        }
    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {

        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_application, parent, false)

        return when (viewType) {
            AppType.BASIC.intId -> BasicAppViewHolder(inflater, callback)
            AppType.EMPTY.intId -> EmptyAppViewHolder(inflater, callback)
            else -> EmptyAppViewHolder(inflater, callback)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).appType.intId
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        holder.itemView.layoutParams.apply {
            height = itemSize
            width = itemSize
        }
        holder.itemView.setPadding((itemSize * DadigActivity.ICON_PADDING_RATIO).toInt())

        when (getItem(position).appType) {
            AppType.BASIC -> (holder as BasicAppViewHolder).bind(getItem(position) as BasicApp)
            AppType.EMPTY -> (holder as EmptyAppViewHolder).bind(getItem(position) as EmptyApp)
            else -> (holder as EmptyAppViewHolder).bind(getItem(position) as EmptyApp)
        }
    }

    abstract class ParentViewHolder(itemView: View, cb: ((ClickCallbackCommand) -> Unit)? = null) :
        RecyclerView.ViewHolder(itemView) {
        lateinit var app: ParentApp

        init {
            cb?.let {
                itemView.setOnLongClickListener {
                    cb.invoke(ClickCallbackCommand(itemView, adapterPosition, ClickType.LONG_CLICK))
                    false
                }

                itemView.setOnClickListener {
                    cb.invoke(ClickCallbackCommand(itemView, adapterPosition, ClickType.CLICK))
                }
            }
        }
    }

    class BasicAppViewHolder(itemView: View, cb: ((ClickCallbackCommand) -> Unit)? = null) : ParentViewHolder(itemView, cb) {

        val ivIcon = itemView.itemApplicationIvIcon


        fun bind(basicApp: BasicApp) {
            app = basicApp
            ivIcon.setImageDrawable(AppUtil.getIconDrawableFromPkgName(itemView.context, basicApp.pkgName))
        }
    }

    class EmptyAppViewHolder(itemView: View, cb: ((ClickCallbackCommand) -> Unit)? = null) : ParentViewHolder(itemView, cb) {

        val ivIcon = itemView.itemApplicationIvIcon
        fun bind(emptyApp: EmptyApp) {
            app = emptyApp

        }
    }

    data class ClickCallbackCommand(
        val itemView: View,
        val position: Int,
        val type: ClickType
    )

    enum class ClickType {
        CLICK,
        LONG_CLICK
    }

}