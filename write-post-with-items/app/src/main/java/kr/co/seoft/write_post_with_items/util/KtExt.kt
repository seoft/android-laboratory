package kr.co.seoft.write_post_with_items.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.google.gson.Gson
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

fun String.toaste(context: Context, tag: String = "", duration: Int = Toast.LENGTH_LONG): Toast {
    this.e("$tag#$#")
    return Toast.makeText(context, this, duration).apply { show() }
}

fun String.toast(context: Context, duration: Int = Toast.LENGTH_LONG): Toast {
    return Toast.makeText(context, this, duration).apply { show() }
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

fun String?.exist(completion: (String) -> Unit): String? {
    return if (this.isNullOrBlank()) {
        null
    } else {
        completion(this)
        this
    }
}

// Any extentions
fun Any.e(tag: String = "") {
    Log.e("$tag#$#", this.toString())
}

fun Any.i(tag: String = "") {
    Log.i("$tag#$#", this.toString())
}

fun AppCompatActivity.setupActionBar(@IdRes toolbarId: Int, action: ActionBar.() -> Unit) {
    setSupportActionBar(findViewById(toolbarId))
    supportActionBar?.run {
        action()
    }
}

fun Int.pxToDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}

fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

fun Float.pxToDp(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}

fun Float.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

fun Boolean.toVisibleOrGone() = if (this) View.VISIBLE else View.GONE

fun Boolean.toVisible() = if (this) View.VISIBLE else View.INVISIBLE

fun Int.dimen(context: Context) = context.resources.getDimension(this).toInt()

val String.Companion.EMPTY get() = ""
val String.Companion.HIDE_LABEL_AND_ICON get() = "HIDE_LABEL_AND_ICON" // 폴더나 하단 뷰때 애매하게 보이는 아이콘/텍스트를 숨기기위함


fun <T : Any> T.toJson() = Gson().toJson(this)
fun <T : Any> String.fromJson(classType: Class<T>): T? = Gson().fromJson<T>(this, classType as Type)
fun <T : Any> String.fromJson(type: Type): T? = Gson().fromJson<T>(this, type)

fun ImageView.setTint(context: Context, @ColorRes color: Int) {
    this.imageTintList = AppCompatResources.getColorStateList(context, color)
//    this.imageTintList = context.resources.getColorStateList(color, null)
}

//fun ImageView.setEnableColor(context: Context, isEnable: Boolean) {
//    this.setTint(context, if (isEnable) R.color.pIconEnableGray else R.color.pIconDisableGray)
//}
//
//fun ImageView.setBlueGrayOrDisableGray(context: Context, isBlueGray: Boolean) {
//    this.setTint(context, if (isBlueGray) R.color.pBlueGray else R.color.pIconDisableGray)
//}

/**
 * ~ 10초 : 지금 막
 * ~ 60초 : n초 전
 * ~ 60분 : n분 전
 * ~ 24시 : n시간 전
 * ~ 48시 : 어제
 * ~ 7일 : n일 전
 * ~ : MM.DD
 */
fun Date.convert(): String {
    val diff = (Date().time - this.time) / 1000
    return when (diff) {
        in 0 until 10 -> "지금 막"
        in 10 until 60 -> "${diff}초 전"
        in 60 until 60 * 60 -> "${diff / 60}분 전"
        in 60 * 60 until 60 * 60 * 24 -> "${diff / (60 * 60)}시간 전"
        in 60 * 60 * 24 until 60 * 60 * 48 -> "어제"
        in 60 * 60 * 24 until 60 * 60 * 24 * 7 -> "${diff / (60 * 60 * 24)}일 전"
        else -> SimpleDateFormat("MM.dd", Locale.getDefault()).format(this)
    }
}

fun Context.getColorById(colorId: Int): Int {
    return Color.parseColor(this.resources.getString(colorId))
}

fun Context.getDimenById(dimenId: Int): Int {
    return resources.getDimension(dimenId).toInt()
}