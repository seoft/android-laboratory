package kr.co.seoft.antonio_sample.ui.antonio

import io.github.naverz.antonio.databinding.AntonioBindingModel
import kr.co.seoft.antonio_sample.BR
import kr.co.seoft.antonio_sample.R

sealed class AntonioUiModel(
    val type: Type,
    open val id: Long,
    open val price: Int
) : AntonioBindingModel {
    enum class Type { MONITOR, MOUSE, PHONE }

    override fun bindingVariableId(): Int = BR.item

    data class Monitor(override val id: Long, override val price: Int, val inch: Float) :
        AntonioUiModel(Type.MONITOR, id, price) {
        override fun layoutId(): Int = R.layout.item_antonio_monitor
    }

    data class Mouse(override val id: Long, override val price: Int, val buttonCount: Int) :
        AntonioUiModel(Type.MOUSE, id, price) {
        override fun layoutId(): Int = R.layout.item_antonio_mouse
    }

    data class Phone(override val id: Long, override val price: Int, val os: String) :
        AntonioUiModel(Type.PHONE, id, price) {
        override fun layoutId(): Int = R.layout.item_antonio_phone
    }
}