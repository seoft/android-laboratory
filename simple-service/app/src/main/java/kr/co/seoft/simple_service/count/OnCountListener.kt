package kr.co.seoft.simple_service.count

interface OnCountListener {
    fun onSecond(second: Int)
    fun onStatus(status: CountStatus)
}

enum class CountStatus {
    RUNNING, STOP, PAUSE;
}