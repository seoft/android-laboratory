package kr.co.seoft.diff_util_test.ui.test1.auto

import android.os.Handler
import android.os.Looper
import kr.co.seoft.diff_util_test.ui.test1.Test1Activity

class DeletePartAutoProcessor(private val activity: Test1Activity) : BaseAutoProcessor(activity) {
    override val commandCounts: List<Int> =
        listOf(200, 200, 200, 200, 200, 1000, 1000, 1000, 1000, 1000, 2000, 2000, 5000, 5000, 5000)

    override fun playDetail(currentCommandCount: Int) {
        val count = commandCounts.size - currentCommandCount
        if (count < 1) {
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
            activity.setCount(commandCounts[count - 1])
            activity.onDelete()
        }
    }

    override fun initList() {
        activity.setCount(25000)
        activity.onAddSequence()
    }
}