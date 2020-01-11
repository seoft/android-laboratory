package kr.co.seoft.drag_and_drop_between_multiple_grid.model

import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.seoft.drag_and_drop_between_multiple_grid.DadigGridRvAdapter
import kr.co.seoft.drag_and_drop_between_multiple_grid.R
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.AppUtil
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.EMPTY
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.i

enum class AppType(val intId: Int) {
    BASIC(0),
    EMPTY(1),
    FUNCTION(2),
    FOLDER(3),
    PHONE(4),
    SMS(5),
    SETTING(6)
}

open class IconInfo
class BasicInfo(val iv: ImageView) : IconInfo()
class EmptyInfo(val iv: ImageView) : IconInfo()
class FolderInfo(val rv: RecyclerView, val itemSize: Int) : IconInfo()


abstract class ParentApp(open val appType: AppType, open val label: String) {
    abstract fun setIcon(info: IconInfo)

    fun isEmpty(): Boolean {
        return this is EmptyApp
    }

    fun copy(): ParentApp {
        return when (this) {
            is BasicApp -> BasicApp(label, pkgName, appType)
            is EmptyApp -> EmptyApp(label, appType)
            else -> EmptyApp()
        }

    }

}

data class BasicApp(
    override val label: String,
    val pkgName: String,
    override val appType: AppType = AppType.BASIC
) : ParentApp(appType, label) {

    override fun setIcon(info: IconInfo) {
        if (info is BasicInfo) {
            info.iv.setImageDrawable(AppUtil.getIconDrawableFromPkgName(info.iv.context, pkgName))
        }
    }
}

data class EmptyApp(
    override val label: String = String.EMPTY,
    override val appType: AppType = AppType.EMPTY
) : ParentApp(appType, label) {

    override fun setIcon(info: IconInfo) {
        if (info is EmptyInfo) {
            info.iv.setImageResource(R.drawable.ic_add_black_24dp)
        }
    }
}

data class FolderApp(
    val apps: MutableList<ParentApp>,
    override val label: String = "folder",
    override val appType: AppType = AppType.FOLDER
) : ParentApp(appType, label) {

    override fun setIcon(info: IconInfo) {
        if (info is FolderInfo) {
            info.rv.apply {
                setBackgroundResource(R.drawable.bg_folder_square)
            }
            info.rv.layoutManager = object : GridLayoutManager(info.rv.context, 3) {
                override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
                    try {
                        super.onLayoutChildren(recycler, state)
                    } catch (e: Exception) {
                        e.i()
                    }
                }
            }
            info.rv.adapter = DadigGridRvAdapter(info.itemSize).apply {
                submitList(apps)
            }
        }
    }
}