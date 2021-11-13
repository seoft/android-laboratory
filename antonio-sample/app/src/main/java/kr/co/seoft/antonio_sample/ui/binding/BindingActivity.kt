package kr.co.seoft.antonio_sample.ui.binding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.seoft.antonio_sample.BR
import kr.co.seoft.antonio_sample.R
import kr.co.seoft.antonio_sample.databinding.ActivityBindingBinding
import kr.co.seoft.antonio_sample.ui.ProgressDialog
import kr.co.seoft.antonio_sample.ui.normal.NormalUiModel
import kr.co.seoft.antonio_sample.ui.normal.NormalViewModel
import kr.co.seoft.antonio_sample.ui.normal.OnNormalListener
import kr.co.seoft.antonio_sample.util.INTERVAL_LOAD_MORE
import kr.co.seoft.antonio_sample.util.binding_util_for_recyclerview.BindingItem
import kr.co.seoft.antonio_sample.util.binding_util_for_recyclerview.DataBindingAdapter
import kr.co.seoft.antonio_sample.util.binding_util_for_recyclerview.toBindingItem
import kr.co.seoft.antonio_sample.util.toaste
import kr.co.seoft.antonio_sample.util.viewModel

class BindingActivity : AppCompatActivity() {

    private val binding by lazy { ActivityBindingBinding.inflate(layoutInflater) }

    private val viewModel by viewModel { NormalViewModel() }
    private val adapter by lazy {
        DataBindingAdapter<NormalUiModel>(
            bindingVariableId = BR.item,
            variables = mapOf(BR.onNormalListener to onNormalListener),
            object : DiffUtil.ItemCallback<BindingItem<NormalUiModel>>() {
                override fun areItemsTheSame(
                    oldItem: BindingItem<NormalUiModel>, newItem: BindingItem<NormalUiModel>
                ) = oldItem.item.id == newItem.item.id

                override fun areContentsTheSame(
                    oldItem: BindingItem<NormalUiModel>, newItem: BindingItem<NormalUiModel>
                ) = oldItem == newItem
            })
    }
    private val layoutManager by lazy { LinearLayoutManager(this) }
    private val progress by lazy { ProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.activity = this
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 &&
                    layoutManager.findLastVisibleItemPosition() >
                    viewModel.uiModelsValue.size - INTERVAL_LOAD_MORE
                ) {
                    viewModel.loadMoreUiModels()
                }
            }
        })

        viewModel.throwable.observe(this) {
            it.message?.toaste(this)
        }

        viewModel.uiModels.observe(this) { uiModels ->
            adapter.submitList(
                uiModels.map {
                    when (it) {
                        is NormalUiModel.Monitor -> it.toBindingItem(R.layout.item_normal_monitor)
                        is NormalUiModel.Mouse -> it.toBindingItem(R.layout.item_normal_mouse)
                        is NormalUiModel.Phone -> it.toBindingItem(R.layout.item_normal_phone)
                    }
                }
            )
        }

        viewModel.isShowProgress.observe(this) {
            progress.setVisibleFromBoolean(it)
        }

        viewModel.loadMoreUiModels()
    }

    private val onNormalListener = object : OnNormalListener {
        override fun onClicked(item: NormalUiModel) {
            item.toString().toaste(this@BindingActivity)
        }
    }
}


