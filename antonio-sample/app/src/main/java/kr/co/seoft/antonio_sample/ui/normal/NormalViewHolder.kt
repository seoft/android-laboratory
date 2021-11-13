package kr.co.seoft.antonio_sample.ui.normal

import kr.co.seoft.antonio_sample.databinding.ItemNormalMonitorBinding
import kr.co.seoft.antonio_sample.databinding.ItemNormalMouseBinding
import kr.co.seoft.antonio_sample.databinding.ItemNormalPhoneBinding
import kr.co.seoft.antonio_sample.util.BindViewHolder


class NormalMonitorViewHolder(
    private val binding: ItemNormalMonitorBinding, onNormalListener: OnNormalListener
) : BindViewHolder<NormalUiModel>(binding.root) {

    init {
        binding.onNormalListener = onNormalListener
    }

    override fun bind(item: NormalUiModel) {
        if (item !is NormalUiModel.Monitor) return
        binding.item = item
    }
}

class NormalMouseViewHolder(
    private val binding: ItemNormalMouseBinding, onNormalListener: OnNormalListener
) : BindViewHolder<NormalUiModel>(binding.root) {

    init {
        binding.onNormalListener = onNormalListener
    }

    override fun bind(item: NormalUiModel) {
        if (item !is NormalUiModel.Mouse) return
        binding.item = item
    }
}

class NormalPhoneViewHolder(
    private val binding: ItemNormalPhoneBinding, onNormalListener: OnNormalListener
) : BindViewHolder<NormalUiModel>(binding.root) {

    init {
        binding.onNormalListener = onNormalListener
    }

    override fun bind(item: NormalUiModel) {
        if (item !is NormalUiModel.Phone) return
        binding.item = item
    }
}