package kr.co.seoft.write_post_with_items.ui.wirte.vote

object VoteData {
    sealed class VoteItem {
        data class Title(val text: String) : VoteItem()
        data class Content(val id: Int, val text: String) : VoteItem()
        object OptionEdit : VoteItem()
        data class OptionMultiple(val isMultiple: Boolean) : VoteItem()
        data class OptionOverlap(val isOverlap: Boolean) : VoteItem()
    }
}