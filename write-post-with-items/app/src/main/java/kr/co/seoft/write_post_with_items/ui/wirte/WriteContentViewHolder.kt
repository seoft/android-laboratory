package kr.co.seoft.write_post_with_items.ui.wirte

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_write_content_blank.view.*
import kotlinx.android.synthetic.main.item_write_content_text.view.*
import kr.co.seoft.write_post_with_items.databinding.*
import kr.co.seoft.write_post_with_items.ui.dialog.SimpleSelectDialog
import kr.co.seoft.write_post_with_items.util.EMPTY
import kr.co.seoft.write_post_with_items.util.toEditable

object WriteContentViewHolder {

    class WriteContentTextViewHolder(
        private val binding: ItemWriteContentTextBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel): RecyclerView.ViewHolder {
                return WriteContentTextViewHolder(
                    ItemWriteContentTextBinding.inflate(LayoutInflater.from(parent.context), parent, false), writeViewModel
                )
            }
        }

        private val editText = itemView.itemWriteContentTextEditText

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Text) return
            binding.viewModel = writeViewModel
            binding.contentText = item
            binding.executePendingBindings()
        }

        fun clearFocus() {
            editText.clearFocus()
        }
    }


    class WriteContentImageViewHolder(
        private val binding: ItemWriteContentImageBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel): RecyclerView.ViewHolder {
                return WriteContentImageViewHolder(
                    ItemWriteContentImageBinding.inflate(LayoutInflater.from(parent.context), parent, false), writeViewModel
                )
            }
        }

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Image) return
            binding.viewModel = writeViewModel
            binding.contentImage = item
            binding.executePendingBindings()
        }
    }

    class WriteContentVoteViewHolder(
        private val binding: ItemWriteContentVoteBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel): RecyclerView.ViewHolder {
                return WriteContentVoteViewHolder(
                    ItemWriteContentVoteBinding.inflate(LayoutInflater.from(parent.context), parent, false), writeViewModel
                )
            }
        }

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.VoteContent) return
            binding.viewModel = writeViewModel
            binding.contentVote = item
            binding.executePendingBindings()
        }
    }

    class WriteContentTodoViewHolder(
        private val binding: ItemWriteContentTodoBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel): RecyclerView.ViewHolder {
                return WriteContentTodoViewHolder(
                    ItemWriteContentTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false), writeViewModel
                )
            }
        }

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Todo) return
            binding.viewModel = writeViewModel
            binding.contentTodo = item
            binding.executePendingBindings()
        }
    }

    class WriteContentYoutubeViewHolder(
        private val binding: ItemWriteContentYoutubeBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel): RecyclerView.ViewHolder {
                return WriteContentYoutubeViewHolder(
                    ItemWriteContentYoutubeBinding.inflate(LayoutInflater.from(parent.context), parent, false), writeViewModel
                )
            }
        }

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Youtube) return
            binding.viewModel = writeViewModel
            binding.contentYoutube = item
            binding.executePendingBindings()
        }
    }

    class WriteContentBlankViewHolder(
        private val binding: ItemWriteContentBlankBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel): RecyclerView.ViewHolder {
                return WriteContentBlankViewHolder(
                    ItemWriteContentBlankBinding.inflate(LayoutInflater.from(parent.context), parent, false), writeViewModel
                )
            }
        }

        private val editText = itemView.itemWriteContentBlankEditText

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Blank) return
            binding.viewModel = writeViewModel
            binding.contentBlank = item
            binding.executePendingBindings()
            editText.text = String.EMPTY.toEditable()
        }

        fun clearFocus() {
            editText.clearFocus()
        }
    }

    class WriteShuffleTextViewHolder(
        private val binding: ItemWriteShuffleTextBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel): RecyclerView.ViewHolder {
                return WriteShuffleTextViewHolder(
                    ItemWriteShuffleTextBinding.inflate(LayoutInflater.from(parent.context), parent, false), writeViewModel
                )
            }
        }

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Text) return
            binding.viewModel = writeViewModel
            binding.contentText = item
            binding.viewHolder = this
            binding.executePendingBindings()
        }
    }

    class WriteShuffleImageViewHolder(
        private val binding: ItemWriteShuffleImageBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel): RecyclerView.ViewHolder {
                return WriteShuffleImageViewHolder(
                    ItemWriteShuffleImageBinding.inflate(LayoutInflater.from(parent.context), parent, false), writeViewModel
                )
            }
        }

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Image) return
            binding.viewModel = writeViewModel
            binding.contentImage = item
            binding.viewHolder = this
            binding.executePendingBindings()
        }
    }

    class WriteShuffleVoteViewHolder(
        private val binding: ItemWriteShuffleVoteBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel): RecyclerView.ViewHolder {
                return WriteShuffleVoteViewHolder(
                    ItemWriteShuffleVoteBinding.inflate(LayoutInflater.from(parent.context), parent, false), writeViewModel
                )
            }
        }

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.VoteContent) return
            binding.viewModel = writeViewModel
            binding.contentVote = item
            binding.viewHolder = this
            binding.executePendingBindings()
        }
    }

    class WriteShuffleTodoViewHolder(
        private val binding: ItemWriteShuffleTodoBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel): RecyclerView.ViewHolder {
                return WriteShuffleTodoViewHolder(
                    ItemWriteShuffleTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false), writeViewModel
                )
            }
        }

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Todo) return
            binding.viewModel = writeViewModel
            binding.contentTodo = item
            binding.viewHolder = this
            binding.executePendingBindings()
        }
    }

    class WriteShuffleYoutubeViewHolder(
        private val binding: ItemWriteShuffleYoutubeBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel): RecyclerView.ViewHolder {
                return WriteShuffleYoutubeViewHolder(
                    ItemWriteShuffleYoutubeBinding.inflate(LayoutInflater.from(parent.context), parent, false), writeViewModel
                )
            }
        }

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Youtube) return
            binding.viewModel = writeViewModel
            binding.contentYoutube = item
            binding.viewHolder = this
            binding.executePendingBindings()
        }
    }

    object WriteContentViewHolderHelper {
        fun showSelectDialog(context: Context, writeViewModel: WriteViewModel, content: WriteData.Content) {
            writeViewModel.editTextsFocusOff.set(true)
            SimpleSelectDialog(
                context, listOf(
                    SimpleSelectDialog.SimpleSelectItem("수정") {
                        writeViewModel.editContent.set(content)
                    },
                    SimpleSelectDialog.SimpleSelectItem("삭제") {
                        writeViewModel.removeItem(content)
                    })
            ).show()
        }
    }
}