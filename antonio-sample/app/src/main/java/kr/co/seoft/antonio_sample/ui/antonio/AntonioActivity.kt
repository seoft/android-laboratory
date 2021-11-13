package kr.co.seoft.antonio_sample.ui.antonio

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.naverz.antonio.core.state.SubmittableRecyclerViewState
import io.github.naverz.antonio.databinding.setState
import kr.co.seoft.antonio_sample.BR
import kr.co.seoft.antonio_sample.databinding.ActivityAntonioBinding
import kr.co.seoft.antonio_sample.ui.ProgressDialog
import kr.co.seoft.antonio_sample.ui.normal.*
import kr.co.seoft.antonio_sample.util.INTERVAL_LOAD_MORE
import kr.co.seoft.antonio_sample.util.toaste
import kr.co.seoft.antonio_sample.util.viewModel

class AntonioActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAntonioBinding.inflate(layoutInflater) }

    private val viewModel by viewModel { AntonioViewModel() }
    private val layoutManager by lazy { LinearLayoutManager(this) }
    private val progress by lazy { ProgressDialog(this) }
    private val recyclerViewState = SubmittableRecyclerViewState(
        object : DiffUtil.ItemCallback<AntonioUiModel>() {
            override fun areItemsTheSame(oldItem: AntonioUiModel, newItem: AntonioUiModel) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: AntonioUiModel, newItem: AntonioUiModel) =
                oldItem == newItem
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.activity = this
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.setState(
            recyclerViewState,
            additionalVariables = mapOf(BR.onAntonioListener to onAntonioListener)
        )
        binding.recyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
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

        viewModel.uiModels.observe(this) {
            recyclerViewState.submitList(it)
        }

        viewModel.isShowProgress.observe(this) {
            progress.setVisibleFromBoolean(it)
        }

        viewModel.loadMoreUiModels()
    }

    private val onAntonioListener = object : OnAntonioListener {
        override fun onClicked(item: AntonioUiModel) {
            item.toString().toaste(this@AntonioActivity)
        }
    }
}