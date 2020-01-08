package kr.co.seoft.drag_and_drop_between_multiple_grid.model

import android.content.Context
import android.graphics.drawable.Drawable
import kr.co.seoft.drag_and_drop_between_multiple_grid.R
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.AppUtil

enum class AppType(val intId: Int) {
    BASIC(0),
    EMPTY(1),
    FUNCTION(2),
    FOLDER(3),
    PHONE(4),
    SMS(5),
    SETTING(6)
}

abstract class ParentApp(open val appType: AppType, open val label: String) {
    abstract fun getImage(context: Context): Drawable

    fun isEmpty(): Boolean {
        return this is EmptyApp
    }

}

data class BasicApp(
    override val label: String,
    val pkgName: String,
    override val appType: AppType = AppType.BASIC
) : ParentApp(appType, label) {

    override fun getImage(context: Context): Drawable {
        return AppUtil.getIconDrawableFromPkgName(context, pkgName)
    }
}

data class EmptyApp(
    override val label: String,
    override val appType: AppType = AppType.EMPTY
) : ParentApp(appType, label) {

    override fun getImage(context: Context): Drawable {
        return context.resources.getDrawable(R.drawable.ic_add_black_24dp, null)
    }
}