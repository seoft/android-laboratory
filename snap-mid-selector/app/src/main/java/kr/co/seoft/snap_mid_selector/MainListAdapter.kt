package kr.co.seoft.snap_mid_selector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.seoft.snap_mid_selector.databinding.ItemMainBinding

class MainListAdapter(private val mainViewModel: MainViewModel) :
    ListAdapter<ItemModel, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<ItemModel>() {
        override fun areItemsTheSame(
            oldItem: ItemModel,
            newItem: ItemModel
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ItemModel,
            newItem: ItemModel
        ) = oldItem.isSelect == newItem.isSelect

        override fun getChangePayload(
            oldItem: ItemModel,
            newItem: ItemModel
        ): Any = 0

    }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MainViewHolder(
            ItemMainBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), mainViewModel
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? MainViewHolder)?.setData(getItem(position))
    }

}