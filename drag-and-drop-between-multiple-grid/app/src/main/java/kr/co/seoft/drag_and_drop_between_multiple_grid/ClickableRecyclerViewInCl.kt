package kr.co.seoft.drag_and_drop_between_multiple_grid

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView

class ClickableRecyclerViewInCl @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    val rv = RecyclerView(context)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }


    init {
        rv.id = View.generateViewId()
        this.addView(rv)
        ConstraintSet().also { cs ->
            cs.clone(this)
            cs.connect(
                rv.id,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT,
                0
            )
            cs.connect(
                rv.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
            )
            cs.applyTo(this)
        }

        rv.layoutParams.apply {
            width = LayoutParams.MATCH_PARENT
            height = LayoutParams.MATCH_PARENT
        }
    }
}