package kr.co.seoft.snap_mid_selector

import androidx.recyclerview.widget.RecyclerView
import kr.co.seoft.snap_mid_selector.databinding.ItemMainBinding

class MainViewHolder(
    private val binding: ItemMainBinding,
    private val mainViewModel: MainViewModel
) : RecyclerView.ViewHolder(binding.root) {

    fun setData(item: ItemModel) {
        binding.item = item
        binding.mainViewModel = mainViewModel
    }

}