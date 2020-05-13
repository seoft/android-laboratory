package kr.co.seoft.write_post_with_items.ui.wirte

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_write.*
import kr.co.seoft.write_post_with_items.R
import kr.co.seoft.write_post_with_items.databinding.ActivityWriteBinding
import kr.co.seoft.write_post_with_items.ui.wirte.vote.VoteActivity
import kr.co.seoft.write_post_with_items.util.ImageUtil
import kr.co.seoft.write_post_with_items.util.SC
import kr.co.seoft.write_post_with_items.util.toast
import kr.co.seoft.write_post_with_items.util.toaste

class WriteActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, WriteActivity::class.java))
        }

        const val DELAY_OF_BLANK_CONVERT_TO_TEXT = 50L
    }

    private lateinit var binding: ActivityWriteBinding
    private val writeViewModel by lazy { ViewModelProvider(this).get(WriteViewModel::class.java) }
    private val adapter by lazy { WriteContentAdapter(writeViewModel) }
    private val layoutManager by lazy { LinearLayoutManager(this) }
    private val itemTouchHelper by lazy {
        ItemTouchHelper(ItemMoveCallback(moveCallback = { from, to ->
            // 첫 index 의 Content.Text 아이템 경우 제외될때 사이드 이펙트 방지로 예외처리
            if (from.adapterPosition == 0 || to.adapterPosition == 0) return@ItemMoveCallback
            writeViewModel.swapList(from.adapterPosition, to.adapterPosition)
        }))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_write)
        binding.act = this
        binding.writeViewModel = writeViewModel
        binding.lifecycleOwner = this

        initView()
        initObserver()
        initListener()

    }

    private fun initView() {
        actWriteRvList.adapter = adapter
        actWriteRvList.layoutManager = layoutManager
        itemTouchHelper.attachToRecyclerView(actWriteRvList)
    }

    private fun initObserver() {
        writeViewModel.resultList.observe(this, Observer {
            adapter.submitList(it) {
                if (writeViewModel.isAddedItemToLast.getAndSet(false)) layoutManager.scrollToPosition(adapter.itemCount - 1)
            }
        })

        writeViewModel.dragItem.observe(this, Observer {
            itemTouchHelper.startDrag(it)
        })

        writeViewModel.editTextsFocusOff.observe(this, Observer {
            adapter.notifyItemRangeChanged(0, adapter.itemCount, WriteContentAdapter.ALL_EDIT_TEXT_FOCUS_OFF)
        })

        writeViewModel.editTextsFocusOffAndStartShuffle.observe(this, Observer {
            adapter.notifyItemRangeChanged(0, adapter.itemCount, WriteContentAdapter.ALL_EDIT_TEXT_FOCUS_OFF)
            Handler().postDelayed({ writeViewModel.setShuffleMode() }, DELAY_OF_BLANK_CONVERT_TO_TEXT)
        })

    }

    private fun initListener() {

    }

    fun onClickIvBack() {
        "onClickIvBack".toast(this)
    }

    fun onClickTvComplete() {
        "onClickTvComplete".toast(this)
    }

    fun onClickTvShuffleComplete() {
        writeViewModel.unsetShuffleMode()
    }

    fun onClickIvImageIcon() {
        ImageUtil.requestPermissionGrant(this)
    }

    fun onClickIvVoteIcon() {
        // 추가과정 생략
//        writeViewModel.addItemToLast(WriteData.Content.Vote(writeViewModel.random.nextLong().toString()))
        VoteActivity.startActivity(this)
    }

    fun onClickIvTodoIcon() {
        // 추가과정 생략
        writeViewModel.addItemToLast(WriteData.Content.Todo(writeViewModel.random.nextLong().toString()))
    }

    fun onClickIvYoutubeIcon() {
        // 추가과정 생략
        writeViewModel.addItemToLast(WriteData.Content.Youtube(writeViewModel.random.nextLong().toString()))
    }

    fun onClickIvEmojiImageIcon() {
        "임시제외".toaste(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        ImageUtil.runIfPermissionGranted(SC.PermissionsResult(requestCode, permissions, grantResults)) {
            ImageUtil.selectMediaFile(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (requestCode == WriteVoteActivity.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
//            data.getParcelableExtra<CreateVote>(WriteVoteActivity.EXTRA_RESULT_VOTE)?.let {
//                viewModel.createVote.set(it)
//            }
//        } else if (requestCode == SearchActivity.TAG_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
//            val resultKeyword = data.getStringExtra(SearchActivity.EXTRA_RESULT_KEYWORD)
//            viewModel.tags.set((viewModel.tags.value ?: emptyList()) + resultKeyword)
//        }

        ImageUtil.getContentUri(this, SC.ActivityResult(requestCode, resultCode, data))?.let {
            writeViewModel.addImageAfterResize(it)
        }
    }

}
