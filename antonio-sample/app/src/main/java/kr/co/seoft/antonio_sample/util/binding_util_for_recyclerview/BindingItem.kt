package kr.co.seoft.antonio_sample.util.binding_util_for_recyclerview

import androidx.annotation.LayoutRes

data class BindingItem<ITEM>(@LayoutRes val type: Int, val item: ITEM)

fun <T> T.toBindingItem(@LayoutRes type: Int): BindingItem<T> {
    return BindingItem(type, this)
}