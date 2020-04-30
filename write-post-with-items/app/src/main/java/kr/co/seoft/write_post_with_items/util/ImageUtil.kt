package kr.co.seoft.write_post_with_items.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.SystemClock
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import java.io.File
import kotlin.random.Random

object ImageUtil {

    private const val REQ_CODE_SELECT_IMAGE = 7329
    private const val PERMISSION_READ_WRITE_EXTERNAL_STORAGE_CODE = 3452

    fun selectMediaFile(activity: Activity) {
        val intent = Intent(Intent.ACTION_PICK).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        intent.type = "image/*"
        intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        activity.startActivityForResult(intent, REQ_CODE_SELECT_IMAGE)
    }

    fun getContentUri(context: Context, activityResult: SC.ActivityResult): File? {
        if (activityResult.resultCode == Activity.RESULT_OK &&
            activityResult.requestCode == REQ_CODE_SELECT_IMAGE &&
            activityResult.data != null
        ) {
            return getFileFromUri(context, activityResult.data.data ?: return null)
        }
        return null
    }

    private fun getFileFromUri(context: Context, contentUri: Uri): File? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        context.contentResolver.query(contentUri, projection, null, null, null).use {
            it ?: return null
            it.moveToFirst()
            val selectedImagePath = it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
            return File(selectedImagePath)
        }
    }

    fun requestPermissionGrant(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_READ_WRITE_EXTERNAL_STORAGE_CODE
        )
    }

    fun runIfPermissionGranted(permissionsResult: SC.PermissionsResult, callback: () -> Unit) {
        if (permissionsResult.requestCode == PERMISSION_READ_WRITE_EXTERNAL_STORAGE_CODE &&
            permissionsResult.grantResults.isNotEmpty() &&
            permissionsResult.grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        ) {
            callback.invoke()
        }
    }

    fun formatToJpgToCacheDir(from: File, to: File, percent: Int = 80) {
        val rawBitmap = BitmapFactory.decodeFile(from.path)
        to.outputStream().use {
            rawBitmap.compress(Bitmap.CompressFormat.JPEG, percent, it)
            rawBitmap.recycle()
        }
    }

    // ref : https://developer.android.com/topic/performance/graphics/load-bitmap?hl=ko
    private fun calculateInSampleSize(options: BitmapFactory.Options, maxSize: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        if (height > maxSize || width > maxSize) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= maxSize && halfWidth / inSampleSize >= maxSize) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun resizeImageToCacheDir(context: Context, file: File, maxSize: Int): File {
        val bmOptions = BitmapFactory.Options()
        bmOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(file.absolutePath, bmOptions)
        bmOptions.inSampleSize = calculateInSampleSize(bmOptions, maxSize)
        bmOptions.inJustDecodeBounds = false

        val resizedFile = File(context.cacheDir.path, Random(SystemClock.currentThreadTimeMillis()).nextLong().toString()).apply {
            createNewFile()
        }
        val resizedBitmap = BitmapFactory.decodeFile(file.path, bmOptions)
        resizedFile.outputStream().use {
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, it)
            resizedBitmap.recycle()
        }
        return resizedFile
    }
}