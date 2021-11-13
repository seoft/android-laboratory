package kr.co.seoft.antonio_sample.ui.normal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.co.seoft.antonio_sample.databinding.ItemNormalMonitorBinding
import kr.co.seoft.antonio_sample.databinding.ItemNormalMouseBinding
import kr.co.seoft.antonio_sample.databinding.ItemNormalPhoneBinding
import kr.co.seoft.antonio_sample.util.BindViewHolder

class NormalAdapter(
    private val onNormalListener: OnNormalListener
) : ListAdapter<NormalUiModel, BindViewHolder<NormalUiModel>>(
    object : DiffUtil.ItemCallback<NormalUiModel>() {
        override fun areItemsTheSame(oldItem: NormalUiModel, newItem: NormalUiModel) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NormalUiModel, newItem: NormalUiModel) =
            oldItem == newItem
    }
) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.ordinal
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): BindViewHolder<NormalUiModel> {
        return when (NormalUiModel.Type.values()[viewType]) {
            NormalUiModel.Type.MONITOR -> NormalMonitorViewHolder(
                ItemNormalMonitorBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), onNormalListener
            )
            NormalUiModel.Type.MOUSE -> NormalMouseViewHolder(
                ItemNormalMouseBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), onNormalListener
            )
            NormalUiModel.Type.PHONE -> NormalPhoneViewHolder(
                ItemNormalPhoneBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                ), onNormalListener
            )
        }
    }

    override fun onBindViewHolder(holder: BindViewHolder<NormalUiModel>, position: Int) {
        holder.bind(getItem(position))
    }
}