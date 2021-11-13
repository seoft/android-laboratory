package kr.co.seoft.antonio_sample.util

interface OnBind<in T> {
    fun bind(item: T)
}
