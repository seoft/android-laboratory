package kr.co.seoft.diff_util_test.ui.test1.auto

import android.os.Handler
import android.os.Looper
import kr.co.seoft.diff_util_test.ui.test1.Test1Activity

class AddSequenceAutoProcessor(private val activity: Test1Activity) : BaseAutoProcessor(activity) {
    override val commandCounts: List<Int> =
        listOf(200, 200, 200, 200, 200, 1000, 1000, 1000, 1000, 1000, 2000, 2000, 5000, 5000, 5000)

    override fun playDetail(currentCommandCount: Int) {
        if (currentCommandCount >= commandCounts.size) {
            autoPlaying = false
            activity.onResult()
            clearCommandInfo()
            if (playCount < fullCount) {
                playCount++
                Handler(Looper.getMainLooper()).postDelayed({
                    play()
                }, 300L)
            }
        } else {
            activity.setCount(commandCounts[currentCommandCount])
            activity.onAddSequence()
        }
    }

    override fun initList() {
        activity.onClearList()
    }
}