package kr.co.seoft.drag_and_drop_between_multiple_grid.util

import android.app.Activity
import android.util.DisplayMetrics

object DimensionUtil {

    fun getDeviceWidthAndHeight(activity: Activity): Pair<Int, Int> {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }




}