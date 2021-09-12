package kr.co.seoft.diff_util_test.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BindViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView), OnBind<T>
