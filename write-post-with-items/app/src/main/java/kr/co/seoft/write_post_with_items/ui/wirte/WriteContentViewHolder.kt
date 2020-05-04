package kr.co.seoft.write_post_with_items.ui.wirte

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_write_content_blank.view.*
import kotlinx.android.synthetic.main.item_write_content_text.view.*
import kr.co.seoft.write_post_with_items.R
import kr.co.seoft.write_post_with_items.ViewDetectable
import kr.co.seoft.write_post_with_items.databinding.*
import kr.co.seoft.write_post_with_items.ui.dialog.SimpleSelectDialog
import kr.co.seoft.write_post_with_items.util.EMPTY
import kr.co.seoft.write_post_with_items.util.dpToPx
import kr.co.seoft.write_post_with_items.util.toEditable
import kr.co.seoft.write_post_with_items.util.toast

object WriteContentViewHolder {

    class WriteContentTextViewHolder(itemView: View, private val writeViewModel: WriteViewModel) :
        RecyclerView.ViewHolder(itemView),
        ViewDetectable {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel): RecyclerView.ViewHolder {
                return WriteContentTextViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_write_content_text, parent, false),
                    writeViewModel
                )
            }
        }

        private val editText = itemView.itemWriteContentTextEditText

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Text) return
            editText.text = item.text.toEditable()
            editText.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    // 첫 index 의 Content.Text 아이템 경우 제외될때 사이드 이펙트 방지로 예외처리
                    if (editText.text.toString().isEmpty() && item.id != WriteViewModel.TOP_TEXT_ID) {
                        writeViewModel.removeItem(item)
                    } else {
                        writeViewModel.setTextItem(item.copy(text = editText.text.toString()))
                    }
                }
            }
        }

        private val editTextsFocusOffObserver = Observer<Boolean> {
            editText.isEnabled = false
            editText.isEnabled = true
        }

        override fun onViewAttachedToWindow() {
            writeViewModel.editTextsFocusOff.observeForever(editTextsFocusOffObserver)
        }

        override fun onViewDetachedFromWindow() {
            writeViewModel.editTextsFocusOff.removeObserver(editTextsFocusOffObserver)
        }
    }


    class WriteContentImageViewHolder(
        private val binding: ItemWriteContentImageBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root), ViewDetectable {

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

        override fun onViewAttachedToWindow() {}

        override fun onViewDetachedFromWindow() {}
    }

    class WriteContentVoteViewHolder(
        private val binding: ItemWriteContentVoteBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root), ViewDetectable {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel): RecyclerView.ViewHolder {
                return WriteContentVoteViewHolder(
                    ItemWriteContentVoteBinding.inflate(LayoutInflater.from(parent.context), parent, false), writeViewModel
                )
            }
        }

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Vote) return
            binding.viewModel = writeViewModel
            binding.contentVote = item
            binding.executePendingBindings()
        }

        override fun onViewAttachedToWindow() {}

        override fun onViewDetachedFromWindow() {}
    }

    class WriteContentTodoViewHolder(
        private val binding: ItemWriteContentTodoBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root), ViewDetectable {

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

        override fun onViewAttachedToWindow() {}

        override fun onViewDetachedFromWindow() {}
    }

    class WriteContentYoutubeViewHolder(
        private val binding: ItemWriteContentYoutubeBinding,
        private val writeViewModel: WriteViewModel
    ) : RecyclerView.ViewHolder(binding.root), ViewDetectable {

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

        override fun onViewAttachedToWindow() {}

        override fun onViewDetachedFromWindow() {}
    }

    class WriteContentBlankViewHolder(itemView: View, private val writeViewModel: WriteViewModel) :
        RecyclerView.ViewHolder(itemView), ViewDetectable {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel)
                    : RecyclerView.ViewHolder {
                return WriteContentBlankViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_write_content_blank, parent, false), writeViewModel
                )
            }
        }

        private val editText = itemView.itemWriteContentBlankEditText

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Blank) return
            editText.layoutParams.height = 30.dpToPx()
            editText.text = String.EMPTY.toEditable()
            editText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    editText.layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                } else if (!hasFocus && !editText.text.isBlank()) {
                    writeViewModel.addTextItemInsteadBlank(
                        item.previousContent,
                        WriteData.Content.Text(writeViewModel.random.nextInt(), editText.text.toString())
                    )
                } else {
                    editText.layoutParams.height = 30.dpToPx()
                }
                editText.requestLayout()
            }
        }

        private val editTextsFocusOffObserver = Observer<Boolean> {
            editText.isEnabled = false
            editText.isEnabled = true
        }

        override fun onViewAttachedToWindow() {
            writeViewModel.editTextsFocusOff.observeForever(editTextsFocusOffObserver)
        }

        override fun onViewDetachedFromWindow() {
            writeViewModel.editTextsFocusOff.removeObserver(editTextsFocusOffObserver)
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
            if (item !is WriteData.Content.Vote) return
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
                        "수정".toast(context)
                    },
                    SimpleSelectDialog.SimpleSelectItem("삭제") {
                        writeViewModel.removeItem(content)
                    })
            ).show()
        }
    }
}