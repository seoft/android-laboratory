package kr.co.seoft.drag_and_drop_between_multiple_grid.util

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.View
import com.google.gson.Gson
import java.lang.reflect.Type

fun Any.e(tag: String = "#$#") {
    Log.e(tag, this.toString())
}

fun Any.i(tag: String = "#$#") {
    Log.e(tag, this.toString())
}

fun Int.pxToDp() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.dpToPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.dimen(context: Context) = context.resources.getDimension(this).toInt()

val String.Companion.EMPTY get() = ""

fun Boolean.toVisible() = if (this) View.VISIBLE else View.INVISIBLE

fun <T : Any> T.toJson() = Gson().toJson(this)
fun <T : Any> String.fromJson(classType: Class<T>): T? = Gson().fromJson<T>(this, classType as Type)
fun <T : Any> String.fromJson(type: Type): T? = Gson().fromJson<T>(this, type)