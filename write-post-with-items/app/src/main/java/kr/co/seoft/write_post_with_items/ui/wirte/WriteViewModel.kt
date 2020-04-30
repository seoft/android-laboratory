package kr.co.seoft.write_post_with_items.ui.wirte

import android.app.Application
import android.content.Context
import android.os.SystemClock
import android.view.View
import androidx.lifecycle.AndroidViewModel
import kr.co.seoft.write_post_with_items.ui.wirte.WriteData.Content
import kr.co.seoft.write_post_with_items.util.EMPTY
import kr.co.seoft.write_post_with_items.util.ImageUtil
import kr.co.seoft.write_post_with_items.util.SC
import kr.co.seoft.write_post_with_items.util.SafetyLiveData
import java.io.File
import kotlin.random.Random

class WriteViewModel(application: Application) : AndroidViewModel(application) {

    val isPublic = SafetyLiveData<Boolean>().apply {
        set(true)
    }

    val editTextsFocusOff = SafetyLiveData<Boolean>()
    val random by lazy { Random(SystemClock.currentThreadTimeMillis()) }

    val contents = SafetyLiveData<List<Content>>().apply {
        set(listOf(Content.Text(random.nextInt(), String.EMPTY)))
    }

    fun getContents() = contents.value ?: emptyList()

//    var focusIndex: Int = 0

    fun togglePublic(view: View) {
        isPublic.set(!(isPublic.value ?: false))
    }

    fun addImageAfterResize(file: File) {
        val uploadFile = ImageUtil.resizeImageToCacheDir(getApplication() as Context, file, SC.MAX_UPLOAD_IMAGE_SIZE)
        contents.set(getContents() + Content.Image(uploadFile))
    }

    fun addItem(content: Content) {
        contents.set((contents.value ?: emptyList()) + content)
    }

    fun addItem(previousContent: Content, newContent: Content) {
        contents.set(getContents().toMutableList().apply { add(getContents().indexOf(previousContent) + 1, newContent) })
    }

    fun setTextItem(item: Content.Text) {
        contents.set((contents.value ?: emptyList()).map {
            if (it is Content.Text && it.id == item.id) item
            else it
        })
    }

    fun removeItem(removeContent: Content) {
        val removedList = getContents().filter { it != removeContent }
        contents.set(mergeTextIfExist(removedList))
    }

    // 연속 Text아이템 일 경우 중간에 개행을 두고 하나로 합치는 로직
    private fun mergeTextIfExist(curContents: List<Content>): List<Content> {
        for (i in 0 until curContents.size - 1) {
            if (curContents[i] is Content.Text && curContents[i + 1] is Content.Text) {
                return curContents
                    .mapIndexed { index, content ->
                        if (index == i) {
                            val previousText = content as Content.Text
                            val nextText = curContents[i + 1] as Content.Text
                            previousText.copy(text = "${previousText.text}\n${nextText.text}")
                        } else {
                            content
                        }
                    }
                    .filterIndexed { index, _ -> index != i + 1 }
            }
        }
        return curContents
    }

}