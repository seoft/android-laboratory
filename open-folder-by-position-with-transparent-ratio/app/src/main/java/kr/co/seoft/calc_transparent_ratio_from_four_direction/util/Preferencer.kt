package kr.co.seoft.calc_transparent_ratio_from_four_direction.util

import android.content.Context
import androidx.preference.PreferenceManager

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



}