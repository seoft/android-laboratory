package kr.co.seoft.write_post_with_items.ui.wirte

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.seoft.write_post_with_items.ViewDetectable
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteContentBlankViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteContentImageViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteContentTextViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteContentTodoViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteContentVoteViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteContentYoutubeViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteData.Content

class WriteContentAdapter(private val viewModel: WriteViewModel) :
    ListAdapter<Content, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Content>() {
        override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
            return oldItem == newItem
        }
    }) {

    companion object {
        const val WRITE_CONTENT_TEXT = 0
        const val WRITE_CONTENT_IMAGE = 1
        const val WRITE_CONTENT_VOTE = 2
        const val WRITE_CONTENT_TODO = 3
        const val WRITE_CONTENT_YOUTUBE = 4
        const val WRITE_CONTENT_BLANK = 5
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Content.Text -> WRITE_CONTENT_TEXT
            is Content.Image -> WRITE_CONTENT_IMAGE
            is Content.Vote -> WRITE_CONTENT_VOTE
            is Content.Todo -> WRITE_CONTENT_TODO
            is Content.Youtube -> WRITE_CONTENT_YOUTUBE
            is Content.Blank -> WRITE_CONTENT_BLANK
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            WRITE_CONTENT_TEXT -> WriteContentTextViewHolder.getInstance(parent, viewModel)
            WRITE_CONTENT_IMAGE -> WriteContentImageViewHolder.getInstance(parent, viewModel)
            WRITE_CONTENT_VOTE -> WriteContentVoteViewHolder.getInstance(parent, viewModel)
            WRITE_CONTENT_TODO -> WriteContentTodoViewHolder.getInstance(parent, viewModel)
            WRITE_CONTENT_YOUTUBE -> WriteContentYoutubeViewHolder.getInstance(parent, viewModel)
            else /*WRITE_CONTENT_BLANK*/ -> WriteContentBlankViewHolder.getInstance(parent, viewModel)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WriteContentTextViewHolder -> {
                holder.bind(getItem(position))
            }
            is WriteContentImageViewHolder -> {
                holder.bind(getItem(position))
            }
            is WriteContentVoteViewHolder -> {
                holder.bind(getItem(position))
            }
            is WriteContentTodoViewHolder -> {
                holder.bind(getItem(position))
            }
            is WriteContentYoutubeViewHolder -> {
                holder.bind(getItem(position))
            }
            is WriteContentBlankViewHolder -> {
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