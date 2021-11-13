package kr.co.seoft.antonio_sample.ui.antonio

import io.github.naverz.antonio.databinding.AntonioBindingModel
import kr.co.seoft.antonio_sample.BR
import kr.co.seoft.antonio_sample.R
import kr.co.seoft.antonio_sample.data.ResponseModel
import kr.co.seoft.antonio_sample.data.ResponseType

sealed class AntonioUiModel(
    val type: Type,
    open val id: Long,
    open val price: Int
) : AntonioBindingModel {
    enum class Type { MONITOR, MOUSE, PHONE }

    data class Monitor(override val id: Long, override val price: Int, val inch: Float) :
        AntonioUiModel(Type.MONITOR, id, price) {
        override fun bindingVariableId(): Int = BR.item
        override fun layoutId(): Int = R.layout.item_antonio_monitor
    }

    data class Mouse(override val id: Long, override val price: Int, val buttonCount: Int) :
        AntonioUiModel(Type.MOUSE, id, price) {
        override fun bindingVariableId(): Int = BR.item
        override fun layoutId(): Int = R.layout.item_antonio_mouse
    }

    data class Phone(override val id: Long, override val price: Int, val os: String) :
        AntonioUiModel(Type.PHONE, id, price) {
        override fun bindingVariableId(): Int = BR.item
        override fun layoutId(): Int = R.layout.item_antonio_phone
    }

    companion object {

        fun from(responseModels: List<ResponseModel>): List<AntonioUiModel> {
            return responseModels.map { from(it) }
        }

        fun from(responseModel: ResponseModel): AntonioUiModel {
            return responseModel.run {
                when (type) {
                    ResponseType.MONITOR -> Monitor(id, price, inch ?: 0f)
                    ResponseType.MOUSE -> Mouse(id, price, buttonCount ?: 0)
                    ResponseType.PHONE -> Phone(id, price, os ?: "")
                }
            }
        }
    }

}

interface OnAntonioListener {
    fun onClicked(item: AntonioUiModel)
}