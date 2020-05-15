package kr.co.seoft.write_post_with_items.ui.wirte

import org.parceler.Parcel
import org.parceler.ParcelConstructor
import java.io.File

object WriteData {

    sealed class Content(open val id: Int, open val isShuffle: Boolean) {
        data class Image(
            override val id: Int,
            val file: File
            , override val isShuffle: Boolean = false
        ) : Content(id, isShuffle)

        data class VoteContent(
            override val id: Int,
            val vote: Vote,
            override val isShuffle: Boolean = false
        ) :
            Content(id, isShuffle)

        data class Todo(
            override val id: Int,
            val title: String,
            override val isShuffle: Boolean = false
        ) : Content(id, isShuffle)

        data class Youtube(
            override val id: Int,
            val url: String,
            override val isShuffle: Boolean = false
        ) :
            Content(id, isShuffle)

        data class Text(
            override val id: Int,
            val text: String,
            override val isShuffle: Boolean = false
        ) : Content(id, isShuffle)

        data class Blank(
            override val id: Int,
            val previousContent: Content,
            override val isShuffle: Boolean = false
        ) :
            Content(id, isShuffle)

        fun setShuffle(isShuffle: Boolean): Content {
            return when (this) {
                is Text -> this.copy(isShuffle = isShuffle)
                is Image -> this.copy(isShuffle = isShuffle)
                is VoteContent -> this.copy(isShuffle = isShuffle)
                is Todo -> this.copy(isShuffle = isShuffle)
                is Youtube -> this.copy(isShuffle = isShuffle)
                is Blank -> this
            }
        }
    }

    @Parcel
    data class Vote @ParcelConstructor constructor(
        val title: String,
        val voteItems: List<VoteItem>,
        val isMultiple: Boolean,
        val isOverlap: Boolean
    )

    @Parcel
    data class VoteItem @ParcelConstructor constructor(val content: String, val image: File? = null)

}