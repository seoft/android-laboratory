package kr.co.seoft.dots_on_line_view

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

class DotsOnLineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val MIN_WIDTH_DP = 40
        private const val MIN_HEIGHT_DP = 8
    }

    private var lineColor = Color.BLACK
    private var dotColor = Color.BLACK

    var boundary = 0
        set(value) {
            field = value
            invalidate()
        }

    var spotsStringSeparatedComma = ""
        set(value) {
            field = value
            invalidate()
        }

    private var spots: List<Float> = emptyList()

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.DotsOnLineView, 0, 0).apply {
            lineColor = getColor(R.styleable.DotsOnLineView_lineColor, Color.BLACK)
            dotColor = getColor(R.styleable.DotsOnLineView_dotColor, Color.BLACK)
            boundary = getInteger(R.styleable.DotsOnLineView_boundary, 100)
            spotsStringSeparatedComma =
                getString(R.styleable.DotsOnLineView_spotsStringSeparatedComma) ?: ""
        }
    }

    private fun dpToIntPx(dp: Int): Int {
        return dpToIntPx(dp.toFloat())
    }

    private fun dpToIntPx(dp: Float): Int {
        return (dp * Resources.getSystem().displayMetrics.density).roundToInt()
    }

    private fun dpToFloatPx(dp: Int): Float {
        return dpToFloatPx(dp.toFloat())
    }

    private fun dpToFloatPx(dp: Float): Float {
        return (dp * Resources.getSystem().displayMetrics.density)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            MeasureSpec.AT_MOST -> (paddingLeft + paddingRight + dpToIntPx(MIN_WIDTH_DP))
                .coerceAtMost(widthSize)
            else -> widthMeasureSpec
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> (paddingTop + paddingBottom + dpToIntPx(MIN_HEIGHT_DP))
                .coerceAtMost(heightSize)
            MeasureSpec.UNSPECIFIED -> (paddingTop + paddingBottom + dpToIntPx(MIN_HEIGHT_DP))
            else -> heightMeasureSpec
        }

        setMeasuredDimension(width, height)
    }

    private val paint by lazy {
        Paint().apply {
            color = lineColor
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val dp3 = dpToFloatPx(5)
        val width = measuredWidth.toFloat()
        val dotWidth = measuredWidth - (dp3 * 2)
        val dotPaddingWidth = dp3.roundToInt()
        val height = measuredHeight.toFloat()
        try {
            spots = spotsStringSeparatedComma.split(",").map { it.toInt() }.map {
                it / boundary.toFloat() * dotWidth
            }
        } catch (e: Exception) {
            Log.e("DotsOnLineView", "invalid string, Please separate the spots with commas")
            return
        }

        paint.strokeWidth = dpToFloatPx(0.8f)

        val iconSize = dpToFloatPx(5)
        val halfIconSize = (iconSize / 2f).roundToInt()

        val drawable = ContextCompat.getDrawable(context, R.drawable.ic_circle)
        drawable?.setTint(dotColor)

        val iconTop = (measuredHeight / 2) - halfIconSize
        val iconBottom = (measuredHeight / 2) + halfIconSize

        canvas?.drawLine(0f, height / 2, width, height / 2, paint)

        spots.filterNot { it.isNaN() }.forEach {
            drawable?.setBounds(
                dotPaddingWidth + it.roundToInt() - halfIconSize,
                iconTop,
                dotPaddingWidth + it.roundToInt() + halfIconSize,
                iconBottom
            )
            canvas?.let { canvas -> drawable?.draw(canvas) }
        }
    }

}