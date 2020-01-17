package kr.co.seoft.drag_and_drop_between_multiple_grid.model

import android.graphics.Color
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kr.co.seoft.drag_and_drop_between_multiple_grid.DadigActivity.Companion.FOLDER_PREVIEW_COUNT
import kr.co.seoft.drag_and_drop_between_multiple_grid.DadigGridRvAdapter
import kr.co.seoft.drag_and_drop_between_multiple_grid.R
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.AppUtil
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.EMPTY
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.i
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.toJson

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


class ParentAppAdapter : TypeAdapter<ParentApp>() {
    override fun write(writer: JsonWriter, parentApp: ParentApp) {
        val result = when (parentApp.appType) {
            AppType.BASIC -> (parentApp as BasicApp).converToJson()
            AppType.FOLDER -> (parentApp as FolderApp).converToJson()
            AppType.EMPTY -> (parentApp as EmptyApp).converToJson()
            else -> (parentApp as EmptyApp).converToJson()
        }
        writer.jsonValue(result)
    }

    private fun getAppFromJsonReader(reader: JsonReader): ParentApp {

        reader.beginObject()

        var appType = AppType.EMPTY
        var label = String.EMPTY
        var pkgName = String.EMPTY
        var apps = mutableListOf<ParentApp>()

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "appType" -> {
                    appType = AppType.valueOf(reader.nextString())
                }
                "label" -> {
                    label = reader.nextString()
                }
                "pkgName" -> {
                    pkgName = reader.nextString()
                }
                "apps" -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        apps.add(getAppFromJsonReader(reader))
                    }
                    reader.endArray()
                }
            }
        }

        reader.endObject()

        return when (appType) {
            AppType.BASIC -> BasicApp(label, pkgName)
            AppType.FOLDER -> FolderApp(apps, label)
            AppType.EMPTY -> EmptyApp()
            else -> EmptyApp()
        }
    }

    override fun read(reader: JsonReader): ParentApp {
        return getAppFromJsonReader(reader)
    }
}

@JsonAdapter(ParentAppAdapter::class)
abstract class ParentApp(
    open val appType: AppType,
    open val label: String
) {
    abstract fun setIcon(info: IconInfo)

    fun isEmpty(): Boolean {
        return this is EmptyApp
    }

    abstract fun converToJson(): String

    fun copy(): ParentApp {
        return when (this) {
            is BasicApp -> BasicApp(label, pkgName, appType)
            is FolderApp -> FolderApp(apps.map { it.copy() }.toMutableList(), label, appType)
            is EmptyApp -> EmptyApp(label, appType)
            else -> EmptyApp()
        }
    }
}

@JsonAdapter(ParentAppAdapter::class)
data class BasicApp(
    override val label: String,
    val pkgName: String,
    override val appType: AppType = AppType.BASIC
) : ParentApp(appType, label) {

    data class BasicAppForJson(
        val label: String,
        val pkgName: String,
        val appType: AppType
    )

    override fun converToJson(): String {
        return BasicAppForJson(label, pkgName, appType).toJson()
    }

    override fun setIcon(info: IconInfo) {
        if (info is BasicInfo) {
            info.iv.setImageDrawable(AppUtil.getIconDrawableFromPkgName(info.iv.context, pkgName))
        }
    }
}

@JsonAdapter(ParentAppAdapter::class)
data class EmptyApp(
    override val label: String = String.EMPTY,
    override val appType: AppType = AppType.EMPTY
) : ParentApp(appType, label) {

    data class EmptyAppForJson(
        val label: String,
        val appType: AppType
    )

    override fun converToJson(): String {
        return EmptyAppForJson(label, appType).toJson()
    }

    override fun setIcon(info: IconInfo) {
        if (info is EmptyInfo) {
            info.iv.setImageResource(R.drawable.ic_add_black_24dp)
//            info.iv.setImageDrawable(null) // todo open
        }
    }
}

@JsonAdapter(ParentAppAdapter::class)
data class FolderApp(
    val apps: MutableList<ParentApp>,
    override val label: String = "folder",
    override val appType: AppType = AppType.FOLDER
) : ParentApp(appType, label) {

    data class FolderAppForJson(
        val apps: MutableList<ParentApp>,
        val label: String,
        val appType: AppType
    )

    override fun converToJson(): String {
        return FolderAppForJson(apps, label, appType).toJson()
    }

    override fun setIcon(info: IconInfo) {
        if (info is FolderInfo) {
            info.rv.apply {
                setBackgroundColor(Color.parseColor("#123123"))
            }
            info.rv.layoutManager = object : GridLayoutManager(info.rv.context, FOLDER_PREVIEW_COUNT) {
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