package kr.co.seoft.antonio_sample.ui.normal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.seoft.antonio_sample.databinding.ActivityNormalBinding
import kr.co.seoft.antonio_sample.ui.dialog.ProgressDialog
import kr.co.seoft.antonio_sample.util.toaste
import kr.co.seoft.antonio_sample.util.viewModel

class NormalActivity : AppCompatActivity() {

    companion object {
        private const val INTERVAL_LOAD_MORE = 10
    }

    private val binding by lazy { ActivityNormalBinding.inflate(layoutInflater) }

    private val viewModel by viewModel { NormalViewModel() }
    private val adapter by lazy { NormalAdapter(onNormalListener) }
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

        viewModel.uiModels.observe(this) {
            adapter.submitList(it)
        }

        viewModel.isShowProgress.observe(this) {
            progress.setVisibleFromBoolean(it)
        }

        viewModel.loadMoreUiModels()
    }

    private val onNormalListener = object : OnNormalListener {
        override fun onClicked(item: NormalUiModel) {
            item.toString().toaste(this@NormalActivity)
        }
    }
}