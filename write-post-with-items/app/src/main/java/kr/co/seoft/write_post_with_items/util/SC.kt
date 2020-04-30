package kr.co.seoft.write_post_with_items.util

import android.content.Intent

object SC {

    const val INTERVAL_FOR_LOAD_POST = 5
    const val INTERVAL_FOR_LOAD_COMMENT = 10
    const val EXIT_UUID_LENGTH = 20
    const val MAX_UPLOAD_IMAGE_SIZE = 1024

    var removedDocIdsInFragHome: MutableList<Long>? = null
    var removedDocIdInSearch: Long? = null

    data class ActivityResult(val requestCode: Int, val resultCode: Int, val data: Intent?)
    class PermissionsResult(val requestCode: Int, val permissions: Array<out String>, val grantResults: IntArray)
}