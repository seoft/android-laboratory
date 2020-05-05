package kr.co.seoft.write_post_with_items.ui.wirte.vote

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import kr.co.seoft.write_post_with_items.ui.wirte.vote.VoteData.VoteItem
import kr.co.seoft.write_post_with_items.util.*
import kotlin.random.Random


class VoteViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val NO_FOCUSING_TO_CONTENT = -1
    }

    val random = Random(SystemClock.currentThreadTimeMillis())
    val voteItems = SafetyMediatorLiveData<List<VoteItem>>()
    val title = SafetyLiveData<VoteItem.Title>().apply { set(VoteItem.Title(String.EMPTY)) }
    val contens = SafetyLiveData<List<VoteItem.Content>>().apply {
        set(
            listOf(
                VoteItem.Content(random.nextInt(), String.EMPTY),
                VoteItem.Content(random.nextInt(), String.EMPTY),
                VoteItem.Content(random.nextInt(), String.EMPTY)
            )
        )
    }
    val optionEdit = SafetyLiveData<VoteItem.OptionEdit>().apply { set(VoteItem.OptionEdit) }
    val optionMultiple = SafetyLiveData<VoteItem.OptionMultiple>().apply { set(VoteItem.OptionMultiple(false)) }
    val optionOverlap = SafetyLiveData<VoteItem.OptionOverlap>().apply { set(VoteItem.OptionOverlap(false)) }
    val currentContentId = SafetyLiveData<Int>().apply { set(NO_FOCUSING_TO_CONTENT) }
    val canComplete = SafetyLiveData<Boolean>().apply { set(false) }

    init {
        voteItems.addSource(title) {
            "voteItems.addSource(title)".e()
            updateVotesItems()
        }
        voteItems.addSource(contens) { updateVotesItems() }
        voteItems.addSource(optionEdit) { updateVotesItems() }
        voteItems.addSource(optionMultiple) { updateVotesItems() }
        voteItems.addSource(optionOverlap) { updateVotesItems() }
    }

    fun combineVoteItems(): List<VoteItem> {
        return LiveDataUtil.convertToTypeList(title, contens, optionEdit, optionMultiple, optionOverlap) ?: return emptyList()
    }

    fun updateVotesItems() {
        // 타이틀이 비어있거나 항목이 전부 비어있을경우
        canComplete.set(!(title.value?.text.isNullOrEmpty() || contens.value?.all { it.text.isEmpty() } == true))
        voteItems.set(combineVoteItems())
    }

    fun addContent() {
        contens.set((contens.value ?: emptyList()) + VoteItem.Content(random.nextInt(), String.EMPTY))
    }

    fun removeContent(id: Int) {
        contens.set((contens.value ?: emptyList()).filterNot { it.id == id })
    }
}
