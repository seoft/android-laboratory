package kr.co.seoft.write_post_with_items

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
        val list = mutableListOf<String>()

        var index = 0

        // 블랭크는 초기화한다 가정

        list.add("Text")
        list.add("Image")
        list.add("Text")
        list.add("Vote")
        list.add("Text")
        list.add("Todo")
        list.add("Youtube")

        while (list.size - 1 > index) {
            if (list[index] != "Text" && list[index + 1] != "Text") {
                list.add(index + 1, "Blank")
                index++
            }
            index++
        }
        list.add("Blank")

        list.forEach { println(it) }


    }
}
