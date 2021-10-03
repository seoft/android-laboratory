package kr.co.seoft.diff_util_test.ui.test1.auto

import kr.co.seoft.diff_util_test.ui.test1.Test1Activity

abstract class BaseAutoProcessor(private val activity: Test1Activity) {

    abstract val commandCounts: List<Int>
    var autoPlaying = false

    private var commandCount = 0
    protected var fullCount = 0
    protected var playCount = 0

    fun initPreviousPlay(repeatCount: Int = 1) {
        fullCount = repeatCount - 1
        playCount = 0
        clearCommandInfo()
    }

    fun play() {
        if (commandCount == 0) autoPlaying = true
        playDetail(commandCount++)
    }

    protected abstract fun playDetail(currentCommandCount: Int)
    protected abstract fun initList()

    protected fun clearCommandInfo() {
        initList()
        commandCount = 0
        activity.onClearResult()
    }


}