package kr.co.seoft.write_post_with_items.ui.wirte.vote

import android.app.Application
import android.content.Context
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.recyclerview.widget.RecyclerView
import kr.co.seoft.write_post_with_items.ui.wirte.WriteData
import kr.co.seoft.write_post_with_items.ui.wirte.vote.VoteData.VoteItem
import kr.co.seoft.write_post_with_items.util.*
import java.io.File
import kotlin.random.Random

class VoteViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * editText 리스트의 스크롤시 기존 데이터 save,load와 swap 방식 :
     * 1. xml에서 onTextChanged를 통해 [contents] 를 지속적으로 갱신(뷰업데이트는 제외)
     * 2. add, remove, swap 경우 [contents] 변경 후 -> [contentItems] -> [refreshView]
     * 3. xml에서 focus 변경 경우  기존 [contents] -> [contentItems] -> [refreshView]
     */

    companion object {
        const val NO_FOCUSING_TO_CONTENT = -1
    }

    val random = Random(SystemClock.currentThreadTimeMillis())
    val voteItems = SafetyMediatorLiveData<List<VoteItem>>()

    var title = String.EMPTY

    var contents = List(3) { VoteItem.Content(random.nextInt(), String.EMPTY) }
    val contentItems = SafetyLiveData<List<VoteItem.Content>>().apply { set(contents) }

    private val titleItem = VoteItem.Title
    private val divider = VoteItem.Divider
    private val optionEdit = VoteItem.OptionEdit
    val isMultiple = SafetyLiveData<Boolean>().apply { set(false) }
    val isOverlap = SafetyLiveData<Boolean>().apply { set(false) }


    var currentContentId = NO_FOCUSING_TO_CONTENT
    val canComplete = SafetyLiveData<Boolean>().apply { set(false) }
    val scrollToBottom = SafetyLiveData<Boolean>()
    val scrollChanged = SafetyLiveData<Boolean>()
    val showBottomOptionEdit = SafetyLiveData<Boolean>()
    val showDatePickerWithId = SafetyLiveData<Int>()
    val showGallery = SafetyLiveData<Boolean>()
    val showCheckFirstToast = SafetyLiveData<Boolean>()

    val dragItem = SafetyLiveData<RecyclerView.ViewHolder>()
    var dragging = false

    init {
        voteItems.addSource(contentItems) { updateVotesItems() }
        voteItems.addSource(isMultiple) { updateVotesItems() }
        voteItems.addSource(isOverlap) { updateVotesItems() }
    }

    fun combineVoteItems(): List<VoteItem> {
        return listOf(titleItem) + (contentItems.value ?: emptyList()) + divider + optionEdit +
                VoteItem.OptionMultiple(isMultiple.value ?: false) + VoteItem.OptionOverlap(isOverlap.value ?: false)
    }

    fun initVotesIfNeed(vote: WriteData.Vote) {
        title = vote.title
        contents = vote.voteItems.map { VoteItem.Content(random.nextInt(), it.content, it.image) }
        isMultiple.set(vote.isMultiple)
        isOverlap.set(vote.isOverlap)
        contentItems.set(contents)
        refreshCanComplete()
    }

    fun updateVotesItems() {
        voteItems.set(combineVoteItems())
    }

    fun addContent() {
        contents = contents + listOf(VoteItem.Content(random.nextInt(), String.EMPTY))
        contentItems.set(contents)
        scrollToBottom.set(true)
    }

    fun setContent(id: Int, text: String) {
        contents = contents.map {
            if (it.id == id) it.copy(text = text)
            else it
        }
        contentItems.set(contents)
    }

    fun removeContent() {
        if (isSelectingEditText()) return
        contents = contents.filterNot { it.id == currentContentId }
        if (contents.isEmpty()) contents = listOf(VoteItem.Content(random.nextInt(), String.EMPTY))
        contentItems.set(contents)
    }

    fun requestInsertDate() {
        if (isSelectingEditText()) return
        showDatePickerWithId.set(currentContentId)
    }

    private fun isSelectingEditText(): Boolean {
        if (currentContentId == NO_FOCUSING_TO_CONTENT) showCheckFirstToast.set(true)
        return currentContentId == NO_FOCUSING_TO_CONTENT
    }

    fun setTitle(text: CharSequence) {
        title = text.toString()
        refreshCanComplete()
    }

    fun updateContentWithoutViewRefresh(updateContentId: Int, updateText: CharSequence) {
        contents = contents.map {
            if (it.id == updateContentId) it.copy(text = updateText.toString())
            else it
        }
        refreshCanComplete()
    }

    private fun refreshCanComplete() {
        canComplete.set(title.isNotEmpty() && contents.any { it.text.isNotEmpty() || it.image != null })
    }

    fun setFocusingContentId(hasFocus: Boolean, contentId: Int) {
        contentItems.set(contents)
        currentContentId = if (hasFocus) contentId else NO_FOCUSING_TO_CONTENT
    }

    fun swapList(from: Int, to: Int) {
        val nonNullVoteItems = voteItems.value ?: return
        if (nonNullVoteItems[from] !is VoteItem.Content || nonNullVoteItems[to] !is VoteItem.Content) return

        contents = contents.toMutableList().apply {
            swap(from - 1, to - 1) // -1 하는 이유는 타이틀이 1을 차지하기 때문
        }
        contentItems.set(contents)
    }

    fun showGallery() {
        if (isSelectingEditText()) return
        showGallery.set(true)
    }

    fun addImageAfterResize(file: File) {
        if (currentContentId == NO_FOCUSING_TO_CONTENT) return
        val uploadFile = ImageUtil.resizeImageToCacheDir(getApplication() as Context, file, SC.MAX_UPLOAD_IMAGE_SIZE)
        contents = contents.map {
            if (it.id == currentContentId) it.copy(image = uploadFile)
            else it
        }
        contentItems.set(contents)
        refreshCanComplete()
    }

    val onSetDragItem = { vh: RecyclerView.ViewHolder ->
        dragItem.set(vh)
    }

    val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            scrollChanged.set(true)
        }
    }

}
