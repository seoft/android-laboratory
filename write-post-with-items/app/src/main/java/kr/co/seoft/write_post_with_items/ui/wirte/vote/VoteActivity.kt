package kr.co.seoft.write_post_with_items.ui.wirte.vote

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_vote.*
import kr.co.seoft.write_post_with_items.R
import kr.co.seoft.write_post_with_items.databinding.ActivityVoteBinding
import kr.co.seoft.write_post_with_items.ui.wirte.ItemMoveCallback
import kr.co.seoft.write_post_with_items.ui.wirte.WriteData
import kr.co.seoft.write_post_with_items.util.DateUtil
import kr.co.seoft.write_post_with_items.util.SC
import kr.co.seoft.write_post_with_items.util.toaste
import org.parceler.Parcels

class VoteActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE = 1101
        const val EXTRA_RESULT_VOTE_ITEM = "EXTRA_RESULT_VOTE_ITEM"
        const val EXTRA_VOTE = "EXTRA_VOTE"
        const val EXTRA_ID = "EXTRA_ID"
        fun startActivity(activity: Activity) {
            activity.startActivityForResult(Intent(activity, VoteActivity::class.java), REQUEST_CODE)
        }

        fun startActivity(activity: Activity, id: Int, vote: WriteData.Vote) {
            activity.startActivityForResult(Intent(activity, VoteActivity::class.java).apply {
                putExtra(EXTRA_ID, id)
                putExtra(EXTRA_VOTE, Parcels.wrap(vote))
            }, REQUEST_CODE)
        }
    }

    private lateinit var binding: ActivityVoteBinding
    private val voteViewModel by lazy { ViewModelProvider(this).get(VoteViewModel::class.java) }
    private val adapter by lazy { VoteAdapter(voteViewModel) }
    private val layoutManager by lazy { LinearLayoutManager(this) }
    private val itemTouchHelper by lazy {
        ItemTouchHelper(ItemMoveCallback(moveCallback = { from, to ->
            voteViewModel.dragging = true
            voteViewModel.swapList(from.adapterPosition, to.adapterPosition)
        }, endCallback = {
            voteViewModel.dragging = false
        }))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vote)
        binding.act = this
        binding.viewModel = voteViewModel
        binding.lifecycleOwner = this

        initView()
        initObserver()
        initListener()

        Parcels.unwrap<WriteData.Vote>(intent.getParcelableExtra(EXTRA_VOTE))?.let { voteViewModel.initVotesIfNeed(it) }
            ?: voteViewModel.updateVotesItems()
    }

    private fun initView() {
        actVoteRecyclerView.adapter = adapter
        actVoteRecyclerView.layoutManager = layoutManager
        actVoteRecyclerView.itemAnimator = null
        itemTouchHelper.attachToRecyclerView(actVoteRecyclerView)
    }

    private fun initObserver() {
        voteViewModel.voteItems.observe(this, Observer { adapter.submitList(it) })

        voteViewModel.dragItem.observe(this, Observer { itemTouchHelper.startDrag(it) })

        voteViewModel.scrollToBottom.observe(this, Observer {
            layoutManager.scrollToPosition(adapter.itemCount - 1)
        })

        voteViewModel.scrollChanged.observe(this, Observer {
            voteViewModel.showBottomOptionEdit.set(layoutManager.findLastVisibleItemPosition() < adapter.itemCount - 4)
        })

        voteViewModel.showDatePickerWithId.observe(this, Observer { contentId ->
            val date = DateUtil.getToday()
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                voteViewModel.setContent(
                    // example : 2020.5.14(목)
                    contentId, "$year.${month + 1}.$dayOfMonth(${DateUtil.getDayOfWeekFromDate(year, month, dayOfMonth)})"
                )
            }, date.year, date.month, date.day).show()
        })
    }

    private fun initListener() {

    }

    fun onClickIvBack() {
        "onClickIvBack".toaste(this)
    }

    fun onClickTvComplete() {
        val resultVoteItem = WriteData.Vote(
            voteViewModel.title,
            voteViewModel.contents.filterNot { it.text.isEmpty() }.map {
                WriteData.VoteItem(it.text)
            }, voteViewModel.isMultiple.value ?: false,
            voteViewModel.isOverlap.value ?: false
        )
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(EXTRA_ID, intent.getIntExtra(EXTRA_ID, SC.EMPTY_INT))
            putExtra(EXTRA_RESULT_VOTE_ITEM, Parcels.wrap(resultVoteItem))
        })
        finish()
    }
}
