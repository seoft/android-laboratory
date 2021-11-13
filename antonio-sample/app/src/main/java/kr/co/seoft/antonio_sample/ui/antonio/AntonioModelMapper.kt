package kr.co.seoft.antonio_sample.ui.antonio

import kr.co.seoft.antonio_sample.data.ResponseModel
import kr.co.seoft.antonio_sample.data.ResponseType

object AntonioModelMapper {
    fun from(responseModels: List<ResponseModel>): List<AntonioUiModel> {
        return responseModels.map { from(it) }
    }

    fun from(responseModel: ResponseModel): AntonioUiModel {
        return responseModel.run {
            when (type) {
                ResponseType.MONITOR -> AntonioUiModel.Monitor(id, price, inch ?: 0f)
                ResponseType.MOUSE -> AntonioUiModel.Mouse(id, price, buttonCount ?: 0)
                ResponseType.PHONE -> AntonioUiModel.Phone(id, price, os ?: "")
            }
        }
    }
}