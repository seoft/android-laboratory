package kr.co.seoft.diff_util_test.util

interface OnBind<in T> {
    fun bind(item: T)
}
