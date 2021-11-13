package kr.co.seoft.antonio_sample.data

data class ResponseModel(
    val id: Long,
    val type: ResponseType,
    val price: Int,
    val inch: Float? = null,
    val buttonCount: Int? = null,
    val os: String? = null
)

enum class ResponseType { MONITOR, MOUSE, PHONE }