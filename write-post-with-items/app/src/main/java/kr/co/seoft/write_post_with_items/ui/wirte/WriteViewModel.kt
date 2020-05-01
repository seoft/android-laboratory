package kr.co.seoft.write_post_with_items.ui.wirte

import android.app.Application
import android.content.Context
import android.os.SystemClock
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import kr.co.seoft.write_post_with_items.ui.wirte.WriteData.Content
import kr.co.seoft.write_post_with_items.util.*
import java.io.File
import kotlin.random.Random

class WriteViewModel(application: Application) : AndroidViewModel(application) {

    companion object{
        const val TOP_TEXT_ID = 0
    }

    val isPublic = SafetyLiveData<Boolean>().apply {
        set(true)
    }

    val editTextsFocusOff = SafetyLiveData<Boolean>()
    val random by lazy { Random(SystemClock.currentThreadTimeMillis()) }

    val contents = SafetyLiveData<List<Content>>().apply {
        set(listOf(Content.Text(TOP_TEXT_ID, String.EMPTY)))
    }

    val dragItem = SafetyLiveData<RecyclerView.ViewHolder>()

    val isShuffleMode = SafetyLiveData<Boolean>().apply {
        set(false)
    }

    fun getContents() = contents.value ?: emptyList()

    fun togglePublic(view: View) {
        isPublic.set(!(isPublic.value ?: false))
    }

    fun swapList(from: Int, to: Int) {
        contents.set(getContents().toMutableList().apply {
            swap(from, to)
        })
    }

    fun startShuffle() {
        contents.set(getContents().map {
            it.setShuffle(true)
        })
        isShuffleMode.set(true)
    }

    fun completeShuffle() {
        contents.set(mergeTextIfExist(getContents().map {
            it.setShuffle(false)
        }))
        isShuffleMode.set(false)
    }

    fun addImageAfterResize(file: File) {
        val uploadFile = ImageUtil.resizeImageToCacheDir(getApplication() as Context, file, SC.MAX_UPLOAD_IMAGE_SIZE)
        contents.set(getContents() + Content.Image(uploadFile))
    }

    fun addItem(content: Content) {
        contents.set(getContents() + content)
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
    private tailrec fun mergeTextIfExist(curContents: List<Content>): List<Content> {
        for (i in 0 until curContents.size - 1) {
            if (curContents[i] is Content.Text && curContents[i + 1] is Content.Text) {
                return mergeTextIfExist(curContents
                    .mapIndexed { index, content ->
                        if (index == i) {
                            val previousText = content as Content.Text
                            val nextText = curContents[i + 1] as Content.Text
                            previousText.copy(text = "${previousText.text}\n${nextText.text}")
                        } else {
                            content
                        }
                    }
                    .filterIndexed { index, _ -> index != i + 1 })
            }
        }
        return curContents
    }

}