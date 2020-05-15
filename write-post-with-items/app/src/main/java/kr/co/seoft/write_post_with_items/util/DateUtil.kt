package kr.co.seoft.write_post_with_items.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun getToday(): Date {
        Calendar.getInstance().apply {
            return Date(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DAY_OF_MONTH))
        }
    }

    fun getDayOfWeekFromDate(date: Date): String {
        return getDayOfWeekFromDate(date.year, date.month, date.day)
    }

    fun getDayOfWeekFromDate(year: Int, month: Int, day: Int): String {
        val simpleDateFormat = SimpleDateFormat("EEE", Locale.getDefault())
        return simpleDateFormat.format(Calendar.getInstance().apply {
            set(year, month, day)
        }.time)
    }

    // month start from ZERO
    data class Date(val year: Int, val month: Int, val day: Int)

}