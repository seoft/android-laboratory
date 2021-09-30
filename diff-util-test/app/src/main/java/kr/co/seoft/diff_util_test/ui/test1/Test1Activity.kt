package kr.co.seoft.diff_util_test.ui.test1

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kr.co.seoft.diff_util_test.databinding.ActivityTest1Binding
import kr.co.seoft.diff_util_test.util.toaste
import kr.co.seoft.diff_util_test.util.viewModel
import java.util.*

class Test1Activity : AppCompatActivity() {

    private val binding by lazy { ActivityTest1Binding.inflate(layoutInflater) }

    private val viewModel by viewModel { Test1ViewModel() }
    private val adapter by lazy { Test1Adapter(onDeviceListener) }

    var startTime: Long = 0L

    private val count get() = binding.etCount.text.toString().toIntOrNull() ?: 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.activity = this
        binding.viewModel = viewModel

        binding.recyclerView.adapter = adapter

        viewModel.throwable.observe(this) {
            it.message?.toaste(this)
        }

        viewModel.uiModels.observe(this) {
            binding.tvStatus.text = "status : start"
            startTime = Date().time
            adapter.submitList(it) {
                val intervalTime = Date().time - startTime
                if (it.isNotEmpty()) {
                    viewModel.result.add(ResultUiModel(viewModel.latelyType, it.size, intervalTime))
                }
                binding.tvStatus.text = "status : done ($intervalTime)"
            }
        }
    }

    fun onAddSequence() {
        viewModel.addSequenceUiModels(count)
    }

    fun onAddRandom() {
        viewModel.addRandomUiModels(count)
    }

    fun onDelete() {
        viewModel.deleteUiModels(count)
    }

    fun onShuffle() {
        viewModel.shuffleUiModels()
    }

    fun onChange() {
        viewModel.changeUiModels(count)
    }

    fun onResult() {
        ResultDialog(this, viewModel.result).show()
    }

    fun onClear() {
        viewModel.result.clear()
    }

    private val onDeviceListener = object : OnDeviceListener {

        override fun onMonitor(item: DeviceUiModel.Monitor) {
            showItemDialog(item)
        }

        override fun onMouse(item: DeviceUiModel.Mouse) {
            showItemDialog(item)
        }

        override fun onPhone(item: DeviceUiModel.Phone) {
            showItemDialog(item)
        }

    }

    private fun showItemDialog(model: DeviceUiModel) {
        val animals = arrayOf("add only one item", "delete only one item", "change only one item")
        AlertDialog.Builder(this).apply {
            title = "select action"
            setItems(animals) { _, which ->
                when (which) {
                    0 -> viewModel.addOnlyOne(model)
                    1 -> viewModel.deleteOnlyOne(model)
                    2 -> viewModel.changeOnlyOne(model)
                }
            }
            show()
        }
    }
}