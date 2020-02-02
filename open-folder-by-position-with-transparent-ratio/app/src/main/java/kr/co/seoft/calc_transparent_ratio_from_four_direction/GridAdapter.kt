package kr.co.seoft.calc_transparent_ratio_from_four_direction

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_grid.view.*

class GridAdapter(private val itemSize: Int, val cb: ((Int) -> Unit)? = null) :
    ListAdapter<Int, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GridViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_grid,
                parent,
                false
            ), cb
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.layoutParams.apply {
            height = itemSize
            width = itemSize
        }

        (holder as GridViewHolder).bind(getItem(position))
    }

    class GridViewHolder(itemView: View, cb: ((Int) -> Unit)?) : RecyclerView.ViewHolder(itemView) {

        private val llRoot = itemView.itemGridLlRoot

        init {
            itemView.setOnClickListener { cb?.invoke(adapterPosition) }
        }

        fun bind(num: Int) {
            llRoot.setBackgroundColor(num)
        }

    }

}