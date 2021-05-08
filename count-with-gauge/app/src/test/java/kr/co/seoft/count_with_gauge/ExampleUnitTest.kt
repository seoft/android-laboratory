package kr.co.seoft.count_with_gauge

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        val abcd = Abcd().apply {
            println("init2")
        }

        abcd.kk = 3
        abcd.kk = 3
        abcd.kk = 3
        abcd.kk = 3
        abcd.kk = 3

    }

    class Abcd {

        init {
            println("init")

        }

        var kk = 0
            set(value) {
                field = value
                println(value)
            }

    }

}