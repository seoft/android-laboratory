package kr.co.seoft.calc_transparent_ratio_from_four_direction.util

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View

object DimensionUtil {

    fun getDeviceWidthAndHeight(activity: Activity): Pair<Int, Int> {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

    fun getStatusbarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) return context.resources.getDimensionPixelSize(resourceId)
        return 0
    }

    fun getViewPosition(view: View): LeftAndTop {
        val viewPos = IntArray(2)
        view.getLocationOnScreen(viewPos)
        return LeftAndTop(viewPos[0], viewPos[1])
    }

    fun getViewRect(view: View): Rect {
        val tmpPos = getViewPosition(view)
        return Rect(tmpPos.left, tmpPos.top, tmpPos.left + view.width, tmpPos.top + view.height)
    }

    data class LeftAndTop(val left: Int, val top: Int)

}