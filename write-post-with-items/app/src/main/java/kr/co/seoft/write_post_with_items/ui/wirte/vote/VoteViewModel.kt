package kr.co.seoft.write_post_with_items.ui.wirte.vote

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import kr.co.seoft.write_post_with_items.ui.wirte.vote.VoteData.VoteItem
import kr.co.seoft.write_post_with_items.util.EMPTY
import kr.co.seoft.write_post_with_items.util.SafetyLiveData
import kr.co.seoft.write_post_with_items.util.SafetyMediatorLiveData
import kotlin.random.Random

class VoteViewModel(application: Application) : AndroidViewModel(application) {

    val voteItems = SafetyMediatorLiveData<List<VoteItem>>()
    val title = SafetyLiveData<VoteItem.Title>()
    val contens = SafetyLiveData<List<VoteItem.Content>>()
    val optionEdit = SafetyLiveData<VoteItem.OptionEdit>()
    val optionMultiple = SafetyLiveData<VoteItem.OptionMultiple>()
    val optionOverlap = SafetyLiveData<VoteItem.OptionOverlap>()

    val random = Random(SystemClock.currentThreadTimeMillis())


    init {
        voteItems.addSource(title) { updateVotesItems() }
        voteItems.addSource(contens) { updateVotesItems() }
        voteItems.addSource(optionEdit) { updateVotesItems() }
        voteItems.addSource(optionMultiple) { updateVotesItems() }
        voteItems.addSource(optionOverlap) { updateVotesItems() }
    }

    fun combineVoteItems(): List<VoteItem> {
        val currentTitle = title.value ?: VoteItem.Title(String.EMPTY)
        val currentContents = contens.value ?: listOf<VoteItem>(
            VoteItem.Content(random.nextInt(), String.EMPTY),
            VoteItem.Content(random.nextInt(), String.EMPTY),
            VoteItem.Content(random.nextInt(), String.EMPTY)
        )
        val currentOptionEdit = title.value ?: VoteItem.OptionEdit
        val currentOptionMultiple = title.value ?: VoteItem.OptionMultiple(false)
        val currentOptionOverlap = title.value ?: VoteItem.OptionOverlap(false)

        return listOf<VoteItem>(currentTitle) + currentContents + currentOptionEdit + currentOptionMultiple + currentOptionOverlap
    }

    fun updateVotesItems() {
        voteItems.set(combineVoteItems())
    }


}