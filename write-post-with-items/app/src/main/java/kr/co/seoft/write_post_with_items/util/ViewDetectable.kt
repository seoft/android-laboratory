package kr.co.seoft.write_post_with_items.util

interface ViewDetectable {
    fun onViewAttachedToWindow()
    fun onViewDetachedFromWindow()
}