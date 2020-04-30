package kr.co.seoft.write_post_with_items

interface ViewDetectable {
    fun onViewAttachedToWindow()
    fun onViewDetachedFromWindow()
}