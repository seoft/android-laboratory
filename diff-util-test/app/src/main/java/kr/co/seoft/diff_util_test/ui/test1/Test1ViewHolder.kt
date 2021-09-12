package kr.co.seoft.diff_util_test.ui.test1

import kr.co.seoft.diff_util_test.databinding.ItemMonitorBinding
import kr.co.seoft.diff_util_test.databinding.ItemMouseBinding
import kr.co.seoft.diff_util_test.databinding.ItemPhoneBinding
import kr.co.seoft.diff_util_test.util.BindViewHolder

class MonitorViewHolder(
    private val binding: ItemMonitorBinding, onDeviceListener: OnDeviceListener
) : BindViewHolder<DeviceUiModel>(binding.root) {

    init {
        binding.onDeviceListener = onDeviceListener
    }

    override fun bind(item: DeviceUiModel) {
        if (item !is DeviceUiModel.Monitor) return
        binding.item = item
    }

    fun updateInch(inch: Float) {
        binding.tvInch.text = "inch : $inch"
    }
}

class MouseViewHolder(
    private val binding: ItemMouseBinding, onDeviceListener: OnDeviceListener
) : BindViewHolder<DeviceUiModel>(binding.root) {

    init {
        binding.onDeviceListener = onDeviceListener
    }

    override fun bind(item: DeviceUiModel) {
        if (item !is DeviceUiModel.Mouse) return
        binding.item = item
    }

    fun updateButtonCount(buttonCount: Int) {
        binding.tvButtonCount.text = "buttonCount : $buttonCount"
    }
}

class PhoneViewHolder(
    private val binding: ItemPhoneBinding, onDeviceListener: OnDeviceListener
) : BindViewHolder<DeviceUiModel>(binding.root) {

    init {
        binding.onDeviceListener = onDeviceListener
    }

    override fun bind(item: DeviceUiModel) {
        if (item !is DeviceUiModel.Phone) return
        binding.item = item
    }

    fun updateOs(os: String) {
        binding.tvOs.text = "os : $os"
    }
}