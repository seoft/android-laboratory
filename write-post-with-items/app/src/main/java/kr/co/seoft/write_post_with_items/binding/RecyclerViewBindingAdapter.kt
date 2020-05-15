package kr.co.seoft.write_post_with_items.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView


@BindingAdapter("bind:onScrollListener")
fun setOnScrollListener(recyclerView: RecyclerView, listener: RecyclerView.OnScrollListener) {
    recyclerView.addOnScrollListener(listener)
}
