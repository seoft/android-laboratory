package kr.co.seoft.antonio_sample.data

import io.reactivex.Single
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class MockService : MockApi {

    companion object {
        private val random = Random
        private const val LOAD_SIZE = 30
        private const val MAX_PRICE = 1000000000
        private const val MIN_MOCK_LOAD_TIME = 100L
        private const val MAX_MOCK_LOAD_TIME = 500L
        private val osTypes = setOf("android", "ios", "black-berry", "tizen")
    }

    override fun loadMore(): Single<List<ResponseModel>> {
        return Single
            .timer(random.nextLong(MIN_MOCK_LOAD_TIME, MAX_MOCK_LOAD_TIME), TimeUnit.MILLISECONDS)
            .map { createMockResponse() }
    }

    private fun createMockResponse(): List<ResponseModel> {
        fun generateId() = random.nextLong()
        fun generatePrice() = random.nextInt(MAX_PRICE)
        fun generateInch() = random.nextFloat() % 100
        fun generateButtonCount() = random.nextInt(5)
        fun generateRandomOs() = osTypes.random()
        return List(LOAD_SIZE) {
            when (random.nextInt(3)) {
                0 -> {
                    ResponseModel(
                        generateId(),
                        ResponseType.MONITOR,
                        generatePrice(),
                        inch = generateInch()
                    )
                }
                1 -> {
                    ResponseModel(
                        generateId(),
                        ResponseType.MOUSE,
                        generatePrice(),
                        buttonCount = generateButtonCount()
                    )
                }
                2 -> {
                    ResponseModel(
                        generateId(),
                        ResponseType.PHONE,
                        generatePrice(),
                        os = generateRandomOs()
                    )
                }
                else -> error("invalid type")
            }
        }
    }
}