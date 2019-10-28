package kr.co.seoft.left_side_snappy_recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_content.view.*

class ItemRvAdapter(val context: Context, val cb: (VH) -> Unit) :
    RecyclerView.Adapter<ItemRvAdapter.VH>() {

    val EMPTY = ""

    private var items =
        mutableListOf(/*EMPTY,*/ "111", "222", "333", "444", "555", "666", "777"/*, EMPTY, EMPTY*/)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(context)
                .inflate(R.layout.item_content, parent, false),
            cb
        )
            .apply {
                this.itemView.setOnClickListener {
                    cb.invoke(this)
                }
            }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {

        /**
         * set view's style from each HomeBadgeType
         */

        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(getColor(context, R.color.myBlue))
            holder.ivContent.setImageResource(R.drawable.ic_temp_red)
        } else {
            holder.itemView.setBackgroundColor(getColor(context, R.color.myGray))
            holder.ivContent.setImageResource(R.drawable.ic_temp_blue)
        }

        holder.tvContent.text = items[position]
/*        holder.itemView.layoutParams =
            (if (position == 0) LinearLayout.LayoutParams(R.dimen.rvLeftMargin.dimen(context), 0)
            else LinearLayout.LayoutParams(
                R.dimen.itemWidth.dimen(context),
                R.dimen.itemHeight.dimen(context)
            )).apply {
                marginStart = R.dimen.itemMargin.dimen(context)
                marginEnd = R.dimen.itemMargin.dimen(context)
            }*/

/*
        if (items[position] == EMPTY) holder.itemView.visibility = View.INVISIBLE
        else holder.itemView.visibility = View.VISIBLE
*/

    }

    class VH(view: View, cb: (VH) -> Unit) : RecyclerView.ViewHolder(view) {

        init {
            itemView.setOnClickListener {
                cb.invoke(this)
            }
        }

        val tvContent = view.tvContent
        val ivContent = view.ivContent
    }

}