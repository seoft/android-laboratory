package kr.co.seoft.drag_and_drop_between_multiple_grid.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.BasicApp
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.ParentApp

object AppUtil {

    fun getInstalledApps(context: Context): MutableList<ParentApp> {
        val packageManager = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
            .apply { addCategory(Intent.CATEGORY_LAUNCHER) }

        return packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
            .map { it.activityInfo }
            .map { BasicApp(it.loadLabel(packageManager).toString(), it.packageName) }
            .toMutableList()
    }

    fun getIconDrawableFromPkgName(context: Context, pkgName: String): Drawable {
        return context.packageManager.getApplicationIcon(pkgName)
    }


}