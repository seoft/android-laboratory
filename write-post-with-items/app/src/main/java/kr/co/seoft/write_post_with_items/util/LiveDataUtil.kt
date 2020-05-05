package kr.co.seoft.write_post_with_items.util

import androidx.lifecycle.LiveData

object LiveDataUtil {

    /**
     * @param data : list of LiveData<T> or LiveData<List<T>>
     * @return
     * sum of list [LiveData<T> or LiveData<List<T>>]
     * or
     * null(if has null or type error)
     *
     * @example
     * LiveData(*) : LD(*)
     * input : (LD(1), LD(2), LD(List(3,4), LD(5), LD(List(6,7), LD(8))
     * output : ListOf(1,2,3,4,5,6,7,8)
     */
    inline fun <reified T : Any> convertToTypeList(vararg data: LiveData<*>): List<T>? {
        if (data.any { it.value == null }) return null
        return data.map { it.value!! }.map {
            if (it is T) listOf(it)
            else if (it is List<*> && (it.firstOrNull() != null) && it.first() is T) it as List<T>
            else return null
        }.reduce { acc, list -> acc + list }
    }
}