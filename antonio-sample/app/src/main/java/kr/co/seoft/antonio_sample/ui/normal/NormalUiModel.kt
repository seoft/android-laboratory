package kr.co.seoft.antonio_sample.ui.normal

sealed class NormalUiModel(val type: Type, open val id: Long, open val price: Int) {
    enum class Type { MONITOR, MOUSE, PHONE }

    data class Monitor(override val id: Long, override val price: Int, val inch: Float) :
        NormalUiModel(Type.MONITOR, id, price)

    data class Mouse(override val id: Long, override val price: Int, val buttonCount: Int) :
        NormalUiModel(Type.MOUSE, id, price)

    data class Phone(override val id: Long, override val price: Int, val os: String) :
        NormalUiModel(Type.PHONE, id, price)
}