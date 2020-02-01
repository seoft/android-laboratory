package kr.co.seoft.calc_transparent_ratio_from_four_direction.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.google.gson.reflect.TypeToken
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.EmptyApp
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.ParentApp

object Preferencer {

    private const val PREF_APPS_ = "PREF_APPS_"
    private fun appsKey(index: Int) = "$PREF_APPS_$index"

    private const val CURRENT_MEMO = "CURRENT_MEMO"
    private const val COUNT_DOWN = "COUNT_DOWN"

    private fun getPref(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
    private fun getPrefEdit(context: Context) = PreferenceManager.getDefaultSharedPreferences(context).edit()

    fun getCurrentMemo(context: Context): String {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(CURRENT_MEMO, "")!!
    }

    fun setCurrentMemo(context: Context, memo: String) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(CURRENT_MEMO, memo).apply()
    }

    fun getAllApps(context: Context): List<List<ParentApp>> {
        return (0..4).map {
            getApps(context, it)
        }.toList()
    }

    fun getApps(context: Context, index: Int): List<ParentApp> {
        val json = getPref(context).getString(appsKey(index), null)
        if (json != null && json.isNotEmpty()) {
            val rstList = json.fromJson<List<ParentApp>>(object : TypeToken<List<ParentApp>>() {}.type)

            if (rstList != null) {
                return rstList.toList()
            }
        }
        return List(16) {
            EmptyApp()
        }
    }

    fun setAllApps(context: Context, allApps: List<List<ParentApp>>) {
        repeat(4) {
            setApps(context, it, allApps[it])
        }
    }

    fun setApps(context: Context, index: Int, apps: List<ParentApp>) {
        getPrefEdit(context).putString(
            appsKey(index),
            apps.toJson()
        ).commit()
    }


}