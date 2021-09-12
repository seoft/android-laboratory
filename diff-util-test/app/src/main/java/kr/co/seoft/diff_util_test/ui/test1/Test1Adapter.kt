package kr.co.seoft.diff_util_test.ui.test1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kr.co.seoft.diff_util_test.databinding.ItemMonitorBinding
import kr.co.seoft.diff_util_test.databinding.ItemMouseBinding
import kr.co.seoft.diff_util_test.databinding.ItemPhoneBinding
import kr.co.seoft.diff_util_test.util.BindViewHolder

class Test1Adapter(
    private val onDeviceListener: OnDeviceListener
) : ListAdapter<DeviceUiModel, BindViewHolder<DeviceUiModel>>(
    object : DiffUtil.ItemCallback<DeviceUiModel>() {
        override fun areItemsTheSame(oldItem: DeviceUiModel, newItem: DeviceUiModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DeviceUiModel, newItem: DeviceUiModel): Boolean {
            return if (oldItem.price != newItem.price) {
                false
            } else {
                return when {
                    oldItem is DeviceUiModel.Monitor && newItem is DeviceUiModel.Monitor -> {
                        oldItem.inch == newItem.inch
                    }
                    oldItem is DeviceUiModel.Mouse && newItem is DeviceUiModel.Mouse -> {
                        oldItem.buttonCount == newItem.buttonCount
                    }
                    oldItem is DeviceUiModel.Phone && newItem is DeviceUiModel.Phone -> {
                        oldItem.os == newItem.os
                    }
                    else -> true
                }
            }
        }

        override fun getChangePayload(oldItem: DeviceUiModel, newItem: DeviceUiModel): Any? {
            return when (newItem) {
                is DeviceUiModel.Monitor -> {
                    Payload.UpdateMonitorInch(newItem.inch)
                }
                is DeviceUiModel.Mouse -> {
                    Payload.UpdateMouseButtonCount(newItem.buttonCount)
                }
                is DeviceUiModel.Phone -> {
                    Payload.UpdatePhoneOs(newItem.os)
                }
            }
        }

    }
) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type.ordinal
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): BindViewHolder<DeviceUiModel> {
        return when (DeviceUiModel.Type.values()[viewType]) {
            DeviceUiModel.Type.MONITOR -> MonitorViewHolder(
                ItemMonitorBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onDeviceListener
            )
            DeviceUiModel.Type.MOUSE -> MouseViewHolder(
                ItemMouseBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onDeviceListener
            )
            DeviceUiModel.Type.PHONE -> PhoneViewHolder(
                ItemPhoneBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                onDeviceListener
            )
        }
    }

    override fun onBindViewHolder(
        holder: BindViewHolder<DeviceUiModel>, position: Int, payloads: MutableList<Any>
    ) {
        (payloads.firstOrNull() as? Payload)?.let { payload ->
            when (payload) {
                is Payload.UpdateMonitorInch -> {
                    (holder as? MonitorViewHolder)?.updateInch(payload.inch)
                    return
                }
                is Payload.UpdateMouseButtonCount -> {
                    (holder as? MouseViewHolder)?.updateButtonCount(payload.buttonCount)
                    return
                }
                is Payload.UpdatePhoneOs -> {
                    (holder as? PhoneViewHolder)?.updateOs(payload.os)
                    return
                }
            }
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: BindViewHolder<DeviceUiModel>, position: Int) {
        holder.bind(getItem(position))
    }

    sealed class Payload {
        data class UpdateMonitorInch(val inch: Float) : Payload()
        data class UpdateMouseButtonCount(val buttonCount: Int) : Payload()
        data class UpdatePhoneOs(val os: String) : Payload()
    }
}