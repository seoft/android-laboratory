package kr.co.seoft.libfor2min.home

data class HomeBadge(
    var number: Int = -1,
    var second: Int,
    var type: HomeBadgeType
) {
    fun getStringUsingFormat(): String {
        var hour = second / 3600
        var min = second / 60 % 60
        var second_ = second % 60

        return "${if(hour<10) "0$hour" else hour}:${if(min<10) "0$min" else min}:${if(second_<10) "0$second_" else second_}"
    }
}

enum class HomeBadgeType {
    EMPTY,
    FOCUS,
    NORMAL,
    ADD
}

enum class HomeBadgeCallbackType {
    NORMAL_PUSH,
    LONG_PUSH,
    ADD_PUSH,
}

