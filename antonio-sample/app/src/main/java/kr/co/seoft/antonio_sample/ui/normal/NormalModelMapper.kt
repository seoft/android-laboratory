package kr.co.seoft.antonio_sample.ui.normal

import kr.co.seoft.antonio_sample.data.ResponseModel
import kr.co.seoft.antonio_sample.data.ResponseType

object NormalModelMapper {

    fun from(responseModels: List<ResponseModel>): List<NormalUiModel> {
        return responseModels.map { from(it) }
    }

    fun from(responseModel: ResponseModel): NormalUiModel {
        return responseModel.run {
            when (type) {
                ResponseType.MONITOR -> NormalUiModel.Monitor(id, price, inch ?: 0f)
                ResponseType.MOUSE -> NormalUiModel.Mouse(id, price, buttonCount ?: 0)
                ResponseType.PHONE -> NormalUiModel.Phone(id, price, os ?: "")
            }
        }
    }
}
