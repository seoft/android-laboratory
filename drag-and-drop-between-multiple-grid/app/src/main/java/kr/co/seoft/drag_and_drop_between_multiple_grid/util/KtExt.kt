package kr.co.seoft.drag_and_drop_between_multiple_grid.util

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.View

// Any extentions
fun Any.e(tag: String = "#$#") {
    Log.e(tag, this.toString())
}

fun Int.pxToDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}

fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

fun Int.dimen(context: Context): Int {
    return context.resources.getDimension(this).toInt()
}

val String.Companion.EMPTY get() = ""

fun Boolean.toVisible(): Int {
    return if(this) View.VISIBLE else View.INVISIBLE
}