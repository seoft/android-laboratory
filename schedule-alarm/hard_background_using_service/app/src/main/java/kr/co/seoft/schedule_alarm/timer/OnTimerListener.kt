package kr.co.seoft.schedule_alarm.timer

interface OnTimerListener {
    fun onStatusChanged(status: TimerStatus)
    fun onSecondAndIndexChanged(second: Long, index: Int)
}