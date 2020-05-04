package kr.co.seoft.write_post_with_items.binding

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("bind:onLongClick")
fun setOnLongClickListener(view: View, listener: View.OnClickListener) {
    view.setOnLongClickListener { v ->
        listener.onClick(v)
        true
    }
}

@SuppressLint("ClickableViewAccessibility")
@BindingAdapter("bind:viewHolder", "bind:onTouchDownWithViewHolder")
fun setOnTouchListenerWithViewHolder(
    view: View,
    vh: RecyclerView.ViewHolder,
    onTouchDownWithViewHolder: (RecyclerView.ViewHolder) -> Unit
) {
    view.setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) onTouchDownWithViewHolder.invoke(vh)
        true
    }
}
