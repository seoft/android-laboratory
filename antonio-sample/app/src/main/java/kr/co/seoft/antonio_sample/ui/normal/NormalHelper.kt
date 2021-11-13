package kr.co.seoft.antonio_sample.ui.normal

import kr.co.seoft.antonio_sample.data.ResponseModel
import kr.co.seoft.antonio_sample.data.ResponseType

sealed class NormalUiModel(val type: Type, open val id: Long, open val price: Int) {
    enum class Type { MONITOR, MOUSE, PHONE }

    data class Monitor(override val id: Long, override val price: Int, val inch: Float) :
        NormalUiModel(Type.MONITOR, id, price)

    data class Mouse(override val id: Long, override val price: Int, val buttonCount: Int) :
        NormalUiModel(Type.MOUSE, id, price)

    data class Phone(override val id: Long, override val price: Int, val os: String) :
        NormalUiModel(Type.PHONE, id, price)

    companion object {

        fun from(responseModels: List<ResponseModel>): List<NormalUiModel> {
            return responseModels.map { from(it) }
        }

        fun from(responseModel: ResponseModel): NormalUiModel {
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

interface OnNormalListener {
    fun onClicked(item: NormalUiModel)
}