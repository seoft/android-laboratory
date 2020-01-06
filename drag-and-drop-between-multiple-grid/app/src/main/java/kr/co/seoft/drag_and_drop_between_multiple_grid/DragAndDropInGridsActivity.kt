package kr.co.seoft.drag_and_drop_between_multiple_grid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_drag_and_drop_in_grids.*
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.ParentApp
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.AppUtil
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.DimensionUtil
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.dpToPx

class DragAndDropInGridsActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context, gridCount: Int) {

            context.startActivity(Intent(context, DragAndDropInGridsActivity::class.java).apply {
                putExtra(EXTRA_GRID_COUNT, gridCount)
            })
        }

        private const val EXTRA_GRID_COUNT = "EXTRA_GRID_COUNT"
    }

    private val gridCount by lazy { intent.getIntExtra(EXTRA_GRID_COUNT, 0) }

    private var itemSets = mutableListOf<MutableList<ParentApp>>()

    private val centerRvAdapter by lazy {
        DragAndDropInGridsRvAdapter(
            actDadigRvCenter.width / gridCount
        )
    }

    private val bottomRvAdapters by lazy {
        val bottomRvSize = actDadigRvBottom0.width

        listOf(
            DragAndDropInGridsRvAdapter(bottomRvSize / gridCount),
            DragAndDropInGridsRvAdapter(bottomRvSize / gridCount),
            DragAndDropInGridsRvAdapter(bottomRvSize / gridCount),
            DragAndDropInGridsRvAdapter(bottomRvSize / gridCount)
        )
    }

    private val rvBottoms by lazy {
        listOf(
            actDadigRvBottom0,
            actDadigRvBottom1,
            actDadigRvBottom2,
            actDadigRvBottom3
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_and_drop_in_grids)

        initData()

        Handler().post {
            initView()
        }

    }

    private fun initData() {
        val apps = AppUtil.getInstalledApps(baseContext).toMutableList()
        val allGridCount = gridCount * gridCount
        for (i in 0 until 4) {
            itemSets.add(
                apps.drop(i * allGridCount).take(i * allGridCount + allGridCount).toMutableList()
            )
        }
    }

    private fun initView() {

        val widthAndHeight = DimensionUtil.getDeviceWidthAndHeight(this)

        actDadigRvCenter.layoutParams = (actDadigRvCenter.layoutParams as ConstraintLayout.LayoutParams).apply {
            width = widthAndHeight.first - 120.dpToPx()
            height = widthAndHeight.first - 120.dpToPx()
        }

        rvBottoms.forEach {
            it.layoutParams = (it.layoutParams as LinearLayout.LayoutParams).apply {
                height = actDadigRvBottom0.width
            }
        }


        actDadigRvCenter.layoutManager = GridLayoutManager(baseContext, gridCount)
        actDadigRvCenter.adapter = centerRvAdapter
        centerRvAdapter.submitList(itemSets[0])


        rvBottoms.forEachIndexed { index, rv ->
            rv.layoutManager = GridLayoutManager(baseContext, gridCount)
            rv.adapter = bottomRvAdapters[index]

            bottomRvAdapters[index].submitList(itemSets[index])
        }

    }


}
