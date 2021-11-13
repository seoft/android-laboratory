package kr.co.seoft.antonio_sample.util.binding_util_for_recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kr.co.seoft.antonio_sample.util.BindViewHolder

open class DataBindingViewHolder<ITEM>(
    @LayoutRes layoutResId: Int,
    parent: ViewGroup,
    private val bindingVariableId: Int?,
    variables: Map<Int, Any>?
) : BindViewHolder<BindingItem<ITEM>>(
    LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
) {

    private val binding: ViewDataBinding? = DataBindingUtil.bind(itemView)

    init {
        variables?.let {
            for (entry in variables) {
                binding?.setVariable(entry.key, entry.value)
            }
        }
    }

    override fun bind(item: BindingItem<ITEM>) {
        binding ?: return
        binding.setVariable(bindingVariableId ?: return, item.item)
    }
}