package kr.co.seoft.write_post_with_items.ui.wirte.vote

import java.io.File

object VoteData {
    sealed class VoteItem {
        object Title : VoteItem()
        data class Content(val id: Int, val text: String, val image: File? = null) : VoteItem()
        object Divider : VoteItem()
        object OptionEdit : VoteItem()
        data class OptionMultiple(val isMultiple: Boolean) : VoteItem()
        data class OptionOverlap(val isOverlap: Boolean) : VoteItem()
    }
}