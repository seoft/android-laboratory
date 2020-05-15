package kr.co.seoft.write_post_with_items.ui.wirte.vote

object VoteData {
    sealed class VoteItem {
        object Title : VoteItem()
        data class Content(val id: Int, val text: String) : VoteItem()
        object Divider : VoteItem()
        object OptionEdit : VoteItem()
        data class OptionMultiple(val isMultiple: Boolean) : VoteItem()
        data class OptionOverlap(val isOverlap: Boolean) : VoteItem()
    }
}