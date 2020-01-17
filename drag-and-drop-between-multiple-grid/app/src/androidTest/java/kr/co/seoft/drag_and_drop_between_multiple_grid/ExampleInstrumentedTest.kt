package kr.co.seoft.drag_and_drop_between_multiple_grid

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.reflect.TypeToken
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.*
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.Preferencer
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.e
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.fromJson
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.toJson
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("kr.co.seoft.drag_and_drop_between_multiple_grid", appContext.packageName)
    }


    data class B(val a: Int, val b: String, val appType: AppType)

    @Test
    fun testtt() {

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext


        B(333, "B", AppType.BASIC).toJson().e()

    }

    @Test
    fun test() {

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val k = Preferencer.getApps(appContext, 1)


        val kkk = k.toMutableList().apply {
            this[1] = BasicApp("Aa", "Bb")
            this[2] = EmptyApp()
            this[4] = EmptyApp()
            this[6] = BasicApp("A", "B")
            this[8] = FolderApp(
                List(9) {
                    BasicApp("Aa${it}", "Bb${it}")
                }.toMutableList()
            )
        }

        val rst1 = kkk.toJson()

        Preferencer.setApps(appContext, 1, kkk)

        val kk = Preferencer.getApps(appContext, 1)
        "kk $kk".e()

        "FINISH".e()

        val rst2 = kk.toJson()

        assertEquals(rst1, rst2)


    }


    @Test
    fun testaaaaaaaa() {

        val list1 = mutableListOf<ParentApp>().apply {
            add(BasicApp("AAA", "BBB"))
            add(EmptyApp())
            add(BasicApp("CCC", "DDD"))
            add(EmptyApp())
            add(BasicApp("EEE", "FFF"))
            add(BasicApp("GGG", "HHH"))
            add(
                FolderApp(
                    List(9) {
                        BasicApp("Aa${it}", "Bb${it}")
                    }.toMutableList()
                )
            )
        }

        val json1 = list1.toJson()

        val list2 = json1.fromJson<List<ParentApp>>(object : TypeToken<List<ParentApp>>() {}.type)!!

        val json2 = list2.toJson()

        assertEquals(json1, json2)
        println(json1)
        println(json2)

    }


}


