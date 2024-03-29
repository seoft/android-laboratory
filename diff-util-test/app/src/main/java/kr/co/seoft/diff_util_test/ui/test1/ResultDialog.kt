package kr.co.seoft.diff_util_test.ui.test1

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import kr.co.seoft.diff_util_test.databinding.DialogResultBinding
import kr.co.seoft.diff_util_test.util.e
import kotlin.math.roundToInt

class ResultDialog(context: Context, private val models: List<ResultUiModel>) : Dialog(context) {

    private val binding by lazy { DialogResultBinding.inflate(LayoutInflater.from(context)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val size = models.size.toString()
        val average = models.map { it.time }.average()

        "result  : ${models.map { it.time.toString() }.reduce { acc, l -> "$acc\t$l" }}".e()

        binding.titleText.text = "size :$size  /  average : ${average.roundToInt()}"

        binding.listView.adapter = ArrayAdapter(
            context, android.R.layout.simple_list_item_1, models.map {
                "${it.type}  / ${it.size}  / ${it.time}"
            }
        )
    }
}