package kr.co.seoft.write_post_with_items.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.View
import kotlinx.android.synthetic.main.dialog_simple_select.*
import kr.co.seoft.write_post_with_items.R

class SimpleSelectDialog(context: Context, simpleSelectItems: List<SimpleSelectItem>) : Dialog(context) {
    init {
        this.apply {
            setContentView(R.layout.dialog_simple_select)

            val textViews = arrayOf(
                dialogSimpleSelectTv0,
                dialogSimpleSelectTv1,
                dialogSimpleSelectTv2,
                dialogSimpleSelectTv3,
                dialogSimpleSelectTv4,
                dialogSimpleSelectTv5,
                dialogSimpleSelectTv6,
                dialogSimpleSelectTv6
            )
            simpleSelectItems.take(8)
                .forEachIndexed { index, selectItem ->
                    textViews[index].visibility = View.VISIBLE
                    textViews[index].text = selectItem.content
                    textViews[index].setOnClickListener {
                        dismiss()
                        selectItem.callback.invoke()
                    }
                }
        }
    }

    override fun onBackPressed() {
        cancel()
    }

    data class SimpleSelectItem(val content: String, val callback: () -> Unit)
}