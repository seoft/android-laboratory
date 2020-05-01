package kr.co.seoft.write_post_with_items.ui.wirte

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.item_write_content_blank.view.*
import kotlinx.android.synthetic.main.item_write_content_image.view.*
import kotlinx.android.synthetic.main.item_write_content_text.view.*
import kotlinx.android.synthetic.main.item_write_content_todo.view.*
import kotlinx.android.synthetic.main.item_write_content_vote.view.*
import kotlinx.android.synthetic.main.item_write_content_youtube.view.*
import kotlinx.android.synthetic.main.item_write_shuffle_image.view.*
import kotlinx.android.synthetic.main.item_write_shuffle_text.view.*
import kotlinx.android.synthetic.main.item_write_shuffle_todo.view.*
import kotlinx.android.synthetic.main.item_write_shuffle_vote.view.*
import kotlinx.android.synthetic.main.item_write_shuffle_youtube.view.*
import kr.co.seoft.write_post_with_items.R
import kr.co.seoft.write_post_with_items.ViewDetectable
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
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel)
                    : RecyclerView.ViewHolder {
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


    class WriteContentImageViewHolder(itemView: View, private val writeViewModel: WriteViewModel) :
        RecyclerView.ViewHolder(itemView),
        ViewDetectable {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel)
                    : RecyclerView.ViewHolder {
                return WriteContentImageViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_write_content_image, parent, false), writeViewModel
                )
            }
        }

        private val imageView = itemView.itemWriteContentImageView

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Image) return

            Glide.with(itemView.context)
                .load(item.file)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(ColorDrawable(Color.parseColor("#cccccc")))
                .into(imageView)

            itemView.setOnClickListener { WriteContentViewHolderHelper.showSelectDialog(itemView.context, writeViewModel, item) }

            itemView.setOnLongClickListener {
                writeViewModel.editTextsFocusOff.set(true)
                writeViewModel.startShuffle()
                true
            }
        }

        override fun onViewAttachedToWindow() {}

        override fun onViewDetachedFromWindow() {}
    }

    class WriteContentVoteViewHolder(itemView: View, private val writeViewModel: WriteViewModel) :
        RecyclerView.ViewHolder(itemView),
        ViewDetectable {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel)
                    : RecyclerView.ViewHolder {
                return WriteContentVoteViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_write_content_vote, parent, false), writeViewModel
                )
            }
        }

        private val textView = itemView.itemWriteContentVoteTextView

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Vote) return
            textView.text = item.title

            itemView.setOnClickListener { WriteContentViewHolderHelper.showSelectDialog(itemView.context, writeViewModel, item) }

            itemView.setOnLongClickListener {
                writeViewModel.editTextsFocusOff.set(true)
                writeViewModel.startShuffle()
                true
            }
        }

        override fun onViewAttachedToWindow() {}

        override fun onViewDetachedFromWindow() {}
    }

    class WriteContentTodoViewHolder(itemView: View, private val writeViewModel: WriteViewModel) :
        RecyclerView.ViewHolder(itemView),
        ViewDetectable {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel)
                    : RecyclerView.ViewHolder {
                return WriteContentTodoViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_write_content_todo, parent, false), writeViewModel
                )
            }
        }

        private val textView = itemView.itemWriteContentTodoTextView

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Todo) return
            textView.text = item.title

            itemView.setOnClickListener { WriteContentViewHolderHelper.showSelectDialog(itemView.context, writeViewModel, item) }

            itemView.setOnLongClickListener {
                writeViewModel.editTextsFocusOff.set(true)
                writeViewModel.startShuffle()
                true
            }
        }

        override fun onViewAttachedToWindow() {}

        override fun onViewDetachedFromWindow() {}
    }

    class WriteContentYoutubeViewHolder(itemView: View, private val writeViewModel: WriteViewModel) :
        RecyclerView.ViewHolder(itemView),
        ViewDetectable {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel)
                    : RecyclerView.ViewHolder {
                return WriteContentYoutubeViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_write_content_youtube, parent, false),
                    writeViewModel
                )
            }
        }

        private val textView = itemView.itemWriteContentYoutubeTextView

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Youtube) return
            textView.text = item.url

            itemView.setOnClickListener { WriteContentViewHolderHelper.showSelectDialog(itemView.context, writeViewModel, item) }

            itemView.setOnLongClickListener {
                writeViewModel.editTextsFocusOff.set(true)
                writeViewModel.startShuffle()
                true
            }
        }

        override fun onViewAttachedToWindow() {}

        override fun onViewDetachedFromWindow() {}
    }

    class WriteContentBlankViewHolder(itemView: View, private val writeViewModel: WriteViewModel) :
        RecyclerView.ViewHolder(itemView),
        ViewDetectable {

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
                    writeViewModel.addItem(
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

    class WriteShuffleTextViewHolder(itemView: View, private val writeViewModel: WriteViewModel) :
        RecyclerView.ViewHolder(itemView) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel)
                    : RecyclerView.ViewHolder {
                return WriteShuffleTextViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_write_shuffle_text, parent, false),
                    writeViewModel
                )
            }
        }

        private val textView = itemView.itemWriteShuffleTextView

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Text) return
            textView.text = item.text
            itemView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) writeViewModel.dragItem.set(this)
                true
            }
        }
    }

    class WriteShuffleImageViewHolder(itemView: View, private val writeViewModel: WriteViewModel) :
        RecyclerView.ViewHolder(itemView) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel)
                    : RecyclerView.ViewHolder {
                return WriteShuffleImageViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_write_shuffle_image, parent, false),
                    writeViewModel
                )
            }
        }

        private val imageView = itemView.itemWriteShuffleImageView

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Image) return

            Glide.with(itemView.context)
                .load(item.file)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(ColorDrawable(Color.parseColor("#cccccc")))
                .into(imageView)
            itemView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) writeViewModel.dragItem.set(this)
                true
            }
        }
    }

    class WriteShuffleVoteViewHolder(itemView: View, private val writeViewModel: WriteViewModel) :
        RecyclerView.ViewHolder(itemView) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel)
                    : RecyclerView.ViewHolder {
                return WriteShuffleVoteViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_write_shuffle_vote, parent, false),
                    writeViewModel
                )
            }
        }

        private val textView = itemView.itemWriteShuffleVoteTextView

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Vote) return
            textView.text = item.title
            itemView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) writeViewModel.dragItem.set(this)
                true
            }
        }
    }

    class WriteShuffleTodoViewHolder(itemView: View, private val writeViewModel: WriteViewModel) :
        RecyclerView.ViewHolder(itemView) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel)
                    : RecyclerView.ViewHolder {
                return WriteShuffleTodoViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_write_shuffle_todo, parent, false),
                    writeViewModel
                )
            }
        }

        private val textView = itemView.itemWriteShuffleTodoTextView

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Todo) return
            textView.text = item.title
            itemView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) writeViewModel.dragItem.set(this)
                true
            }
        }
    }

    class WriteShuffleYoutubeViewHolder(itemView: View, private val writeViewModel: WriteViewModel) :
        RecyclerView.ViewHolder(itemView) {

        companion object {
            fun getInstance(parent: ViewGroup, writeViewModel: WriteViewModel)
                    : RecyclerView.ViewHolder {
                return WriteShuffleYoutubeViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_write_shuffle_youtube, parent, false),
                    writeViewModel
                )
            }
        }

        private val textView = itemView.itemWriteShuffleYoutubeTextView

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Youtube) return
            textView.text = item.url
            itemView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) writeViewModel.dragItem.set(this)
                true
            }
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