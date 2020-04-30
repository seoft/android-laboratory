package kr.co.seoft.write_post_with_items.ui.wirte

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
                    writeViewModel.setTextItem(item.copy(text = editText.text.toString()))
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

            itemView.setOnClickListener {
                WriteContentViewHolderHelper.showSelectDialog(itemView.context, writeViewModel, item)
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

        private val imageView = itemView.itemWriteContentVoteImageView
        private val cardView = itemView.itemWriteContentVoteCardView
        private val textView = itemView.itemWriteContentVoteTextView

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Vote) return
            textView.text = item.title

            itemView.setOnClickListener {
                WriteContentViewHolderHelper.showSelectDialog(itemView.context, writeViewModel, item)
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

        private val imageView = itemView.itemWriteContentTodoImageView
        private val cardView = itemView.itemWriteContentTodoCardView
        private val textView = itemView.itemWriteContentTodoTextView

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Todo) return
            textView.text = item.title

            itemView.setOnClickListener {
                WriteContentViewHolderHelper.showSelectDialog(itemView.context, writeViewModel, item)
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

        private val imageView = itemView.itemWriteContentYoutubeImageView
        private val cardView = itemView.itemWriteContentYoutubeCardView
        private val textView = itemView.itemWriteContentYoutubeTextView

        fun bind(item: WriteData.Content) {
            if (item !is WriteData.Content.Youtube) return
            textView.text = item.url

            itemView.setOnClickListener {
                WriteContentViewHolderHelper.showSelectDialog(itemView.context, writeViewModel, item)
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
                    editText.layoutParams.height = 60.dpToPx()
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