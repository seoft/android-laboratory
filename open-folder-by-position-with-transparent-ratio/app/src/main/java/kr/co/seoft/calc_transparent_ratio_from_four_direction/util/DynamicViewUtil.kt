package kr.co.seoft.calc_transparent_ratio_from_four_direction.util

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet

object DynamicViewUtil {


    // 자식 뷰 생성 후 id = View.generateViewId() 해줘야함
    fun addViewToConstraintLayout(parentCl: ConstraintLayout, childView: View) {
        parentCl.addView(childView)
        ConstraintSet().apply {
            clone(parentCl)
            connect(
                childView.id,
                ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID,
                ConstraintSet.LEFT,
                0
            )
            connect(
                childView.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.TOP,
                0
            )
            applyTo(parentCl)
        }
    }


}