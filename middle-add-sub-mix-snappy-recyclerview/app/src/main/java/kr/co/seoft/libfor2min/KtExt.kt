package kr.co.seoft.libfor2min

import android.util.Log


fun Any.i(tag: String = "") {
    Log.i("$tag#$#", this.toString())
}
