package kr.co.seoft.write_post_with_items.binding

import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import kr.co.seoft.write_post_with_items.ui.wirte.WriteData
import kr.co.seoft.write_post_with_items.ui.wirte.WriteViewModel
import kr.co.seoft.write_post_with_items.util.dpToPx

@BindingAdapter("bind:contentText", "bind:writeViewModel")
fun setOnFocusChangeListenerWithContentText(
    editText: EditText,
    contentText: WriteData.Content.Text,
    viewModel: WriteViewModel
) {
    editText.setOnFocusChangeListener { _, hasFocus ->
        if (!hasFocus) {
            // 첫 index 의 Content.Text 아이템 경우 제외될때 사이드 이펙트 방지로 예외처리
            if (editText.text.toString().isEmpty() && contentText.id != WriteViewModel.TOP_TEXT_ID) {
                viewModel.removeItem(contentText)
            } else {
                viewModel.setTextItem(contentText.copy(text = editText.text.toString()))
            }
        }
    }
}

@BindingAdapter("bind:blankText", "bind:writeViewModel")
fun setOnFocusChangeListenerWithContentBlank(
    editText: EditText,
    contentBlank: WriteData.Content.Blank,
    viewModel: WriteViewModel
) {
    editText.setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            editText.layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        } else if (!hasFocus && !editText.text.toString().isBlank()) {
            viewModel.addTextItemInsteadBlank(
                contentBlank.previousContent,
                WriteData.Content.Text(viewModel.random.nextInt(), editText.text.toString())
            )
        } else {
            editText.layoutParams.height = 30.dpToPx()
        }
        editText.requestLayout()
    }
}