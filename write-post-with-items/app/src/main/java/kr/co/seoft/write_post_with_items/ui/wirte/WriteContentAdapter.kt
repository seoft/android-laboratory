package kr.co.seoft.write_post_with_items.ui.wirte

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteContentBlankViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteContentImageViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteContentTextViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteContentTodoViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteContentVoteViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteContentYoutubeViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteShuffleImageViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteShuffleTextViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteShuffleTodoViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteShuffleVoteViewHolder
import kr.co.seoft.write_post_with_items.ui.wirte.WriteContentViewHolder.WriteShuffleYoutubeViewHolder
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
        const val ALL_EDIT_TEXT_FOCUS_OFF = "ALL_EDIT_TEXT_FOCUS_OFF"

        const val WRITE_CONTENT_TEXT = 0
        const val WRITE_CONTENT_IMAGE = 1
        const val WRITE_CONTENT_VOTE = 2
        const val WRITE_CONTENT_TODO = 3
        const val WRITE_CONTENT_YOUTUBE = 4
        const val WRITE_CONTENT_BLANK = 5

        const val WRITE_SHUFFLE_TEXT = 10
        const val WRITE_SHUFFLE_IMAGE = 11
        const val WRITE_SHUFFLE_VOTE = 12
        const val WRITE_SHUFFLE_TODO = 13
        const val WRITE_SHUFFLE_YOUTUBE = 14
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Content.Text -> if (getItem(position).isShuffle) WRITE_SHUFFLE_TEXT else WRITE_CONTENT_TEXT
            is Content.Image -> if (getItem(position).isShuffle) WRITE_SHUFFLE_IMAGE else WRITE_CONTENT_IMAGE
            is Content.VoteContent -> if (getItem(position).isShuffle) WRITE_SHUFFLE_VOTE else WRITE_CONTENT_VOTE
            is Content.Todo -> if (getItem(position).isShuffle) WRITE_SHUFFLE_TODO else WRITE_CONTENT_TODO
            is Content.Youtube -> if (getItem(position).isShuffle) WRITE_SHUFFLE_YOUTUBE else WRITE_CONTENT_YOUTUBE
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
            WRITE_CONTENT_BLANK -> WriteContentBlankViewHolder.getInstance(parent, viewModel)
            WRITE_SHUFFLE_TEXT -> WriteShuffleTextViewHolder.getInstance(parent, viewModel)
            WRITE_SHUFFLE_IMAGE -> WriteShuffleImageViewHolder.getInstance(parent, viewModel)
            WRITE_SHUFFLE_VOTE -> WriteShuffleVoteViewHolder.getInstance(parent, viewModel)
            WRITE_SHUFFLE_TODO -> WriteShuffleTodoViewHolder.getInstance(parent, viewModel)
            else /*WRITE_SHUFFLE_YOUTUBE*/ -> WriteShuffleYoutubeViewHolder.getInstance(parent, viewModel)
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
            is WriteShuffleTextViewHolder -> {
                holder.bind(getItem(position))
            }
            is WriteShuffleImageViewHolder -> {
                holder.bind(getItem(position))
            }
            is WriteShuffleVoteViewHolder -> {
                holder.bind(getItem(position))
            }
            is WriteShuffleTodoViewHolder -> {
                holder.bind(getItem(position))
            }
            is WriteShuffleYoutubeViewHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }

        if(payloads.first() == ALL_EDIT_TEXT_FOCUS_OFF){
            when (holder) {
                is WriteContentTextViewHolder -> {
                    holder.clearFocus()
                }
                is WriteContentBlankViewHolder -> {
                    holder.clearFocus()
                }
            }
        }
    }
}