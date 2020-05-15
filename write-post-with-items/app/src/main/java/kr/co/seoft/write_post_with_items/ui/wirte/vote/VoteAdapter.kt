package kr.co.seoft.write_post_with_items.ui.wirte.vote

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.seoft.write_post_with_items.ViewDetectable

class VoteAdapter(private val viewModel: VoteViewModel) :
    ListAdapter<VoteData.VoteItem, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<VoteData.VoteItem>() {
        override fun areItemsTheSame(oldItem: VoteData.VoteItem, newItem: VoteData.VoteItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: VoteData.VoteItem, newItem: VoteData.VoteItem): Boolean {
            return oldItem == newItem
        }
    }) {

    companion object {
        private const val TITLE = 0
        private const val CONTENT = 1
        private const val DIVIDER = 2
        private const val OPTION_EDIT = 3
        private const val OPTION_MULTIPLE = 4
        private const val OPTION_OVERLAP = 5
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is VoteData.VoteItem.Title -> TITLE
            is VoteData.VoteItem.Content -> CONTENT
            is VoteData.VoteItem.Divider -> DIVIDER
            is VoteData.VoteItem.OptionEdit -> OPTION_EDIT
            is VoteData.VoteItem.OptionMultiple -> OPTION_MULTIPLE
            is VoteData.VoteItem.OptionOverlap -> OPTION_OVERLAP
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TITLE -> VoteItemViewHolder.TitleViewHolder.getInstance(parent, viewModel)
            DIVIDER -> VoteItemViewHolder.DividerViewHolder.getInstance(parent)
            CONTENT -> VoteItemViewHolder.ContentViewHolder.getInstance(parent, viewModel)
            OPTION_EDIT -> VoteItemViewHolder.OptionEditViewHolder.getInstance(parent, viewModel)
            OPTION_MULTIPLE -> VoteItemViewHolder.OptionMultipleViewHolder.getInstance(parent, viewModel)
            else /* OPTION_OVERLAP */ -> VoteItemViewHolder.OptionOverlapViewHolder.getInstance(parent, viewModel)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is VoteItemViewHolder.TitleViewHolder -> {
                holder.bind(getItem(position))
            }
            is VoteItemViewHolder.ContentViewHolder -> {
                holder.bind(getItem(position))
            }
            is VoteItemViewHolder.OptionEditViewHolder -> {
                holder.bind(getItem(position))
            }
            is VoteItemViewHolder.OptionMultipleViewHolder -> {
                holder.bind(getItem(position))
            }
            is VoteItemViewHolder.OptionOverlapViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is ViewDetectable) holder.onViewAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is ViewDetectable) holder.onViewDetachedFromWindow()
    }

}