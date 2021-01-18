package kr.co.seoft.schedule_alarm.util

import android.content.Context
import android.content.res.Resources
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.math.roundToInt

fun String.toast(context: Context, duration: Int = Toast.LENGTH_LONG): Toast {
    return Toast.makeText(context, this, duration).apply { show() }
}

fun Any.e(tag: String = "") {
    Log.e("$tag#$#", this.toString())
}

fun Any.i(tag: String = "") {
    Log.i("$tag#$#", this.toString())
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun Int.pxToDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).roundToInt()
}

fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).roundToInt()
}

fun Float.pxToDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).roundToInt()
}

fun Float.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).roundToInt()
}

fun Boolean.toVisibleOrGone() = if (this) View.VISIBLE else View.GONE

fun Boolean.toVisible() = if (this) View.VISIBLE else View.INVISIBLE

inline fun <reified T : Any> T.toJson(gson: Gson = Gson()): String {
    return gson.toJson(this)
}

inline fun <reified T> String.fromJson(gson: Gson = Gson()): T {
    val type = object : TypeToken<T>() {}.type
    return gson.fromJson(this, type)
}