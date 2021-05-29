package kr.co.seoft.snap_mid_selector

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.seoft.snap_mid_selector.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {


    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }
    }

    private val mainViewModel by viewModel { MainViewModel() }
    private val adapter by lazy { MainListAdapter(mainViewModel) }

    private val layoutManager by lazy {
        object : LinearLayoutManager(this, HORIZONTAL, false) {
            override fun onScrollStateChanged(state: Int) {
                super.onScrollStateChanged(state)
                if (state == RecyclerView.SCROLL_STATE_IDLE) {
                    repeat(adapter.itemCount) { count ->
                        getViewRect(findViewByPosition(count) ?: return@repeat).let {
                            if (it.left < centerPositionOfDeviceWidth
                                && centerPositionOfDeviceWidth < it.right
                            ) {
                                mainViewModel.itemsValue.getOrNull(count)
                                    ?.let { item -> mainViewModel.onItem(item, false) }
                            }
                        }
                    }
                }
            }
        }
    }

    private var centerPositionOfDeviceWidth = 0 // 중간에 있는 뷰홀더를 찾아내기 위해 deviceWidth/2 로 할당
    private val centerSnapHelper by lazy { CenterSnapHelper() }
    var initComplete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = mainViewModel
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = this.adapter
        centerSnapHelper.attachToRecyclerView(binding.recyclerView)
        val deviceWidth = screenSize(this@MainActivity).second
        centerPositionOfDeviceWidth = deviceWidth / 2
        val calculatedDp = ((deviceWidth - dp2px(this@MainActivity, 62f)) / 2).roundToInt()
        binding.recyclerView.setPadding(calculatedDp, 0, calculatedDp, 0)
        mainViewModel.items.observe(this,
            Observer { items ->
                if (items.isEmpty()) return@Observer
                adapter.submitList(items) {
                    if (!initComplete) {
                        initComplete = true
                        mainViewModel.onItem(items.random(), true)
                    }
                }
            }
        )

        mainViewModel.selectedItemWithScroll.observe(this, Observer { selectedItemWithScroll ->
            val selectedItem = selectedItemWithScroll.first

            if (selectedItemWithScroll.second) {
                mainViewModel.itemsValue.indexOfFirst {
                    it.id == selectedItem.id
                }.takeIf { it != -1 }?.let {
                    log(it.toString())
                    layoutManager.scrollToPosition(it)
                }
            }
            mainViewModel.refreshItemSelect(selectedItem)
        })

        mainViewModel.onInit()
    }

    fun getViewRect(view: View): Rect {
        val tmpPos = getViewPosition(view)
        return Rect(
            tmpPos.first, tmpPos.second, tmpPos.first + view.width, tmpPos.second + view.height
        )
    }

    private fun getViewPosition(view: View): Pair<Int, Int> {
        val viewPos = IntArray(2)
        view.getLocationOnScreen(viewPos)
        return Pair(viewPos[0], viewPos[1])
    }

    fun screenSize(context: Context): Pair<Int, Int> {
        val metrics = context.resources.displayMetrics
        val height = metrics.heightPixels
        val width = metrics.widthPixels
        return Pair(height, width)
    }

    fun dp2px(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        )
    }

    fun log(string: String) {
        Log.e("#$#", string)
    }
}