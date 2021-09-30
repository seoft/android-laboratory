package kr.co.seoft.diff_util_test.ui.test1

import java.util.*

sealed class DeviceUiModel(val type: Type, open val id: Long, open val price: Int) {
    enum class Type { MONITOR, MOUSE, PHONE }

    data class Monitor(override val id: Long, override val price: Int, val inch: Float) :
        DeviceUiModel(Type.MONITOR, id, price)

    data class Mouse(override val id: Long, override val price: Int, val buttonCount: Int) :
        DeviceUiModel(Type.MOUSE, id, price)

    data class Phone(override val id: Long, override val price: Int, val os: String) :
        DeviceUiModel(Type.PHONE, id, price)

}

interface OnDeviceListener {
    fun onMonitor(item: DeviceUiModel.Monitor)
    fun onMouse(item: DeviceUiModel.Mouse)
    fun onPhone(item: DeviceUiModel.Phone)
}

object Test1Helper {
    private val random = Random()
    private const val MAX_PRICE = 1000000000
    private val osTypes = setOf("android", "ios", "black-berry", "tizen")
    fun getRandomOs(): String {
        return osTypes.random()
    }

    fun createUiModels(size: Int): List<DeviceUiModel> {
        return List(size) {
            when (random.nextInt(3)) {
                0 -> {
                    DeviceUiModel.Monitor(
                        random.nextLong(), random.nextInt(MAX_PRICE), random.nextFloat() % 100
                    )
                }
                1 -> {
                    DeviceUiModel.Mouse(
                        random.nextLong(), random.nextInt(MAX_PRICE), random.nextInt(5)
                    )
                }
                2 -> {
                    DeviceUiModel.Phone(
                        random.nextLong(), random.nextInt(MAX_PRICE), getRandomOs()
                    )
                }
                else -> error("invalid type")
            }
        }
    }
}

fun DeviceUiModel.updateUIModel(): DeviceUiModel {
    return when (this) {
        is DeviceUiModel.Monitor -> {
            var nextInch: Float
            do {
                nextInch = Random().nextFloat() % 100
            } while (nextInch == inch)
            this.copy(inch = nextInch)
        }
        is DeviceUiModel.Mouse -> {
            var nextButtonCount: Int
            do {
                nextButtonCount = Random().nextInt(5)
            } while (nextButtonCount == buttonCount)
            this.copy(buttonCount = nextButtonCount)
        }
        is DeviceUiModel.Phone -> {
            var nextOs: String
            do {
                nextOs = Test1Helper.getRandomOs()
            } while (nextOs == os)
            this.copy(os = nextOs)
        }
    }
}

data class ResultUiModel(val type: String, val size: Int, var time: Long)