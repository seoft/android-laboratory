package kr.co.seoft.write_post_with_items.util

import android.os.Looper
import androidx.lifecycle.MediatorLiveData

class SafetyMediatorLiveData<T> : MediatorLiveData<T>() {

    fun notifyChange() {
        value?.run {
            set(this)
        }
    }

    fun set(value: T) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            setValue(value)
        } else {
            postValue(value)
        }
    }
}