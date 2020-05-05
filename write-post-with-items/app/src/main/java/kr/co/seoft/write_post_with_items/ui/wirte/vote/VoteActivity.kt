package kr.co.seoft.write_post_with_items.ui.wirte.vote

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_vote.*
import kr.co.seoft.write_post_with_items.R
import kr.co.seoft.write_post_with_items.databinding.ActivityVoteBinding

class VoteActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 1101
        fun startActivity(activity: Activity) {
            activity.startActivityForResult(Intent(activity, VoteActivity::class.java), REQUEST_CODE)
        }
    }

    private lateinit var binding: ActivityVoteBinding
    private val voteViewModel by lazy { ViewModelProvider(this).get(VoteViewModel::class.java) }
    private val adapter by lazy { VoteAdapter(voteViewModel) }
    private val layoutManager by lazy { LinearLayoutManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vote)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_vote)
        binding.act = this
        binding.viewModel = voteViewModel
        binding.lifecycleOwner = this

        initView()
        initObserver()
        initListener()

        voteViewModel.updateVotesItems()
    }

    private fun initView() {
        actVoteRecyclerView.adapter = adapter
        actVoteRecyclerView.layoutManager = layoutManager
    }

    private fun initObserver() {

        voteViewModel.voteItems.observe(this, Observer {
            adapter.submitList(it)
        })

    }

    private fun initListener() {

    }

    fun onClickIvBack() {

    }

    fun onClickTvComplete() {

    }

}
