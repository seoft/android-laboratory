package kr.co.seoft.left_side_snappy_recyclerview.util

import android.content.res.Resources
import android.util.Log

// Any extentions
fun Any.i(tag:String = "#$#") {
    Log.i(tag,this.toString())
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

