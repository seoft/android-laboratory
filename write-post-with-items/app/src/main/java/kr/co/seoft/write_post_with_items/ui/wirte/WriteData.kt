package kr.co.seoft.write_post_with_items.ui.wirte

import java.io.File

object WriteData {

    sealed class Content(open val isShuffle: Boolean) {
        data class Image(val file: File, override val isShuffle: Boolean = false) : Content(isShuffle)
        data class Vote(val title: String, override val isShuffle: Boolean = false) : Content(isShuffle)
        data class Todo(val title: String, override val isShuffle: Boolean = false) : Content(isShuffle)
        data class Youtube(val url: String, override val isShuffle: Boolean = false) : Content(isShuffle)
        data class Text(val id: Int, val text: String, override val isShuffle: Boolean = false) : Content(isShuffle)
        data class Blank(val previousContent: Content, override val isShuffle: Boolean = false) : Content(isShuffle)

        fun setShuffle(isShuffle: Boolean): Content {
            return when (this) {
                is Text -> this.copy(isShuffle = isShuffle)
                is Image -> this.copy(isShuffle = isShuffle)
                is Vote -> this.copy(isShuffle = isShuffle)
                is Todo -> this.copy(isShuffle = isShuffle)
                is Youtube -> this.copy(isShuffle = isShuffle)
                is Blank -> this
            }
        }
    }

}