package kr.co.seoft.diff_util_test.util

import android.content.Context
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import kr.co.seoft.diff_util_test.BuildConfig

fun String.toaste(context: Context, tag: String = "", duration: Int = Toast.LENGTH_LONG): Toast {
    this.e("$tag#$#")
    return Toast.makeText(context, this, duration).apply { show() }
}

fun String.toast(context: Context, duration: Int = Toast.LENGTH_LONG): Toast {
    return Toast.makeText(context, this, duration).apply { show() }
}

fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

// Any extentions
fun Any.e(tag: String = "") {
    if (!BuildConfig.DEBUG) return
    Log.e("$tag#$#", this.toString())
}

fun Any.i(tag: String = "") {
    Log.i("$tag#$#", this.toString())
}


fun <T> LiveData<T>.toSingleEvent(): LiveData<T> {
    val result = LiveEvent<T>()
    result.addSource(this) {
        result.value = it
    }
    return result
}
