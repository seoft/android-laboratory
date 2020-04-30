package kr.co.seoft.write_post_with_items.ui.wirte

import java.io.File

object WriteData {

    sealed class Content {
        data class Image(val file: File) : Content()
        data class Vote(val title: String) : Content()
        data class Todo(val title: String) : Content()
        data class Youtube(val url: String) : Content()
        data class Text(val id: Int, val text: String) : Content()
        data class Blank(val previousContent: Content) : Content()
    }

}