package kr.co.seoft.count_with_gauge

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RotateDrawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat

class GaugeCountView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val NOT_USED = -1
        private const val DEFAULT_TEXT_SIZE_DP = 7f
        private const val DEFAULT_BACKGROUND_LINE_COLOR_CODE = "#f3f4f6"
        private const val DEFAULT_LINE_COLOR_CODE = "#5c46ff"
        private const val DEFAULT_TEXT_COLOR = Color.BLACK
        private const val WARNING_COLOR = Color.RED
    }

    var textView: TextView
    var progressBar: ProgressBar
    var progressBackgroundDrawable: GradientDrawable? = null
    var progressDrawable: RotateDrawable? = null

    var count = 0
        set(value) {
            if (field == value) return
            field = value
            refresh()
        }
    var textSize = toPx(DEFAULT_TEXT_SIZE_DP)
        set(value) {
            if (field == value) return
            field = value
            textView.textSize = value
        }
    var backgroundLineColor = Color.parseColor(DEFAULT_BACKGROUND_LINE_COLOR_CODE)
        set(value) {
            if (field == value) return
            field = value
            progressBackgroundDrawable?.setColor(value)
            progressBar.background = progressBackgroundDrawable
        }

    var defaultLineColor = Color.parseColor(DEFAULT_LINE_COLOR_CODE)
        set(value) {
            if (field == value) return
            field = value
            refresh()
        }
    var defaultTextColor = DEFAULT_TEXT_COLOR
        set(value) {
            if (field == value) return
            field = value
            refresh()
        }
    var max = 100
        set(value) {
            if (field == value) return
            field = value
            progressBar.max = value
            refresh()
        }
    var warningBoundary = NOT_USED
        set(value) {
            if (field == value) return
            field = value
            refresh()
        }
    var warningLineColor = WARNING_COLOR
        set(value) {
            if (field == value) return
            field = value
            refresh()
        }
    var warningTextColor = WARNING_COLOR
        set(value) {
            if (field == value) return
            field = value
            refresh()
        }
    var useAnimation = true
        set(value) {
            if (field == value) return
            field = value
            refresh()
        }

    fun refresh() {
        textView.text = count.toString()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            progressBar.setProgress(count, useAnimation)
        } else {
            progressBar.progress = count
        }
        val isWarning = count <= warningBoundary
        progressDrawable?.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                if (isWarning) warningLineColor else defaultLineColor, BlendModeCompat.SRC_ATOP
            )
        progressBar.progressDrawable = progressDrawable

        textView.setTextColor(if (isWarning) warningTextColor else defaultTextColor)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.guage_count_view, this)
        progressBar = findViewById(R.id.progressBar)
        textView = findViewById(R.id.textView)
        progressBackgroundDrawable =
            ContextCompat.getDrawable(
                context,
                R.drawable.gauge_count_background_drawable
            ) as? GradientDrawable
        progressDrawable =
            ContextCompat.getDrawable(
                context,
                R.drawable.gauge_count_progress_drawable
            ) as? RotateDrawable

        context.theme.obtainStyledAttributes(attrs, R.styleable.GaugeCountView, 0, 0).apply {
            try {
                count = getInt(R.styleable.GaugeCountView_count, 100)
                textSize = getDimension(
                    R.styleable.GaugeCountView_textSize,
                    toPx(DEFAULT_TEXT_SIZE_DP)
                )
                backgroundLineColor = getColor(
                    R.styleable.GaugeCountView_backgroundLineColor,
                    Color.parseColor(DEFAULT_BACKGROUND_LINE_COLOR_CODE)
                )
                defaultLineColor = getColor(
                    R.styleable.GaugeCountView_defaultLineColor,
                    Color.parseColor(DEFAULT_LINE_COLOR_CODE)
                )
                defaultTextColor =
                    getColor(R.styleable.GaugeCountView_defaultTextColor, DEFAULT_TEXT_COLOR)
                useAnimation = getBoolean(R.styleable.GaugeCountView_useAnimation, true)
                max = getInt(R.styleable.GaugeCountView_max, 100)
                warningBoundary = getInt(R.styleable.GaugeCountView_warningBoundary, NOT_USED)
                warningLineColor =
                    getColor(R.styleable.GaugeCountView_warningLineColor, WARNING_COLOR)
                warningTextColor =
                    getColor(R.styleable.GaugeCountView_warningTextColor, WARNING_COLOR)
            } finally {
                recycle()
            }
        }
    }

    private fun toPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        )
    }

}