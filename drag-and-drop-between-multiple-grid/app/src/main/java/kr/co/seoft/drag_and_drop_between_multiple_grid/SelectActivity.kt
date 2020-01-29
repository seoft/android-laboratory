package kr.co.seoft.drag_and_drop_between_multiple_grid

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_select.*
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.BasicApp
import kr.co.seoft.drag_and_drop_between_multiple_grid.model.ParentApp
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.AppUtil
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.DimensionUtil
import kr.co.seoft.drag_and_drop_between_multiple_grid.util.e

class SelectActivity : AppCompatActivity() {

    companion object {
        fun startSelectActivity(activity: Activity) {
            activity.startActivityForResult(Intent(activity, SelectActivity::class.java), REQUEST_CODE)
        }

        const val REQUEST_CODE = 1001
        const val EXTRA_RESULT = "EXTRA_RESULT"
    }

    private val selectRvAdapter by lazy {

        SelectRvAdapter(DimensionUtil.getDeviceWidthAndHeight(this).first / 5) {
            when (it.type) {
                SelectType.ABCD -> {

                }
                SelectType.SETTING -> {

                }
                SelectType.APP -> {
                    // 라이브러리 성격이 강한 프로젝트라 BasicApp만 활성화
                    // 다른 타입 또한 ParentApp의 서브타입에 맞게 받은 후 전달해야함
                    val result = (it.basicApp as ParentApp).converToJson()

                    setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(EXTRA_RESULT, result)
                    })

                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        "onCreate selectActivity".e()

        val list = AppUtil.getInstalledApps(this).map {
            (it as BasicApp).let { SelectItem(SelectType.APP, it.label, null, it) }

        }.toMutableList().apply {
            add(0, SelectItem(SelectType.SETTING, "SETTING", R.drawable.ic_temp, null))
            add(0, SelectItem(SelectType.ABCD, "ABCD", R.drawable.bg_folder_square, null))
        }

        actSelectRv.adapter = selectRvAdapter
        actSelectRv.layoutManager = GridLayoutManager(baseContext, 5)
        selectRvAdapter.submitList(list)

    }

    enum class SelectType {
        APP,
        SETTING,
        ABCD;
    }

    // only select 용도, Dadig에 넘겨줄때는 Parent 규격에 맞게
    data class SelectItem(val type: SelectType, val title: String, @DrawableRes val resId: Int?, val basicApp: BasicApp?)
}
