package kr.co.seoft.left_side_snappy_recyclerview.util

import android.content.Context
import android.content.res.Resources
import android.util.Log

// Any extentions
fun Any.i(tag: String = "#$#") {
    Log.i(tag, this.toString())
}


//fun Int.toDp(context: Context) :Int {
//    return this / (context.resources.displayMetrics.densityDpi / 160f)
//}
//
//fun Int.toPx(context: Context) :Int {
//    return this * (context.resources.displayMetrics.densityDpi / 160f)
//}


fun Int.pxToDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}

fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

fun Int.dimen(context: Context): Int {
    return context.resources.getDimension(this).toInt()
}

fun Int.toFormattingString(): String {
    var hour = this / 3600
    var min = this / 60 % 60
    var second_ = this % 60

    return "${if(hour<10) "0$hour" else hour}:${if(min<10) "0$min" else min}:${if(second_<10) "0$second_" else second_}"
}