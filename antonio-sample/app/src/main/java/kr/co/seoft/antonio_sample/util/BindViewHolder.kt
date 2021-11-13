package kr.co.seoft.antonio_sample.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BindViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView), OnBind<T>
