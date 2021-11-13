package kr.co.seoft.antonio_sample.util.binding_util_for_recyclerview

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import kr.co.seoft.antonio_sample.util.BindViewHolder

open class DataBindingAdapter<ITEM>(
    private val bindingVariableId: Int? = null,
    private val variables: Map<Int, Any>? = null,
    diffUtil: DiffUtil.ItemCallback<BindingItem<ITEM>>
) : androidx.recyclerview.widget.ListAdapter<BindingItem<ITEM>,
        BindViewHolder<BindingItem<ITEM>>>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindViewHolder<BindingItem<ITEM>> {
        return DataBindingViewHolder(viewType, parent, bindingVariableId, variables)
    }

    override fun onBindViewHolder(holder: BindViewHolder<BindingItem<ITEM>>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

}