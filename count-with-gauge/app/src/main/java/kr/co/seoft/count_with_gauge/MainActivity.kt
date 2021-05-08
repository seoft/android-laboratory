package kr.co.seoft.count_with_gauge

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    companion object {
        private const val DEFAULT_BACKGROUND_LINE_COLOR_CODE = "#f3f4f6"
        private const val DEFAULT_LINE_COLOR_CODE = "#5c46ff"
        private const val DEFAULT_TEXT_COLOR = Color.BLACK
        private const val WARNING_COLOR = Color.RED
    }

    private val randomGaugeCountViews by lazy {
        listOf(
            findViewById<GaugeCountView>(R.id.gaugeCountView2),
            findViewById<GaugeCountView>(R.id.gaugeCountView3),
            findViewById<GaugeCountView>(R.id.gaugeCountView4),
            findViewById<GaugeCountView>(R.id.gaugeCountView5)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gaugeCountView = findViewById<GaugeCountView>(R.id.gaugeCountView1).also {
            it.count = 50
            it.max = 100
            it.backgroundLineColor = Color.parseColor(DEFAULT_BACKGROUND_LINE_COLOR_CODE)
            it.defaultLineColor = Color.parseColor(DEFAULT_LINE_COLOR_CODE)
            it.textSize = toPx(7f)
            it.defaultTextColor = DEFAULT_TEXT_COLOR
            it.warningBoundary = 20
            it.warningLineColor = WARNING_COLOR
            it.warningTextColor = WARNING_COLOR
        }

        test()

        findViewById<SeekBar>(R.id.seekBar).setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                gaugeCountView.count = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun getRandomColor() =
        listOf(Color.RED, Color.BLACK, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.DKGRAY).random()

    private fun test() {
        randomGaugeCountViews.forEach {
            it.max = Random.nextInt(50, 100)
            it.count = Random.nextInt(0, it.max)
            it.backgroundLineColor = getRandomColor()
            it.defaultLineColor = getRandomColor()
            it.textSize = toPx(Random.nextInt(4, 9).toFloat())
            it.defaultTextColor = getRandomColor()
            it.warningBoundary = Random.nextInt(0, 20)
            it.warningLineColor = getRandomColor()
            it.warningTextColor = getRandomColor()
        }

        Handler(Looper.getMainLooper()).postDelayed({ test() }, 5000L)
    }


    private fun toPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, this.resources.displayMetrics
        )
    }

}