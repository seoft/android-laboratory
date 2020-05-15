package kr.co.seoft.write_post_with_items.ui.wirte.vote

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.seoft.write_post_with_items.databinding.*

object VoteItemViewHolder {

    class TitleViewHolder(
        private val binding: ItemVoteTitleBinding,
        private val voteViewModel: VoteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, voteViewModel: VoteViewModel): RecyclerView.ViewHolder {
                return TitleViewHolder(
                    ItemVoteTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false), voteViewModel
                )
            }
        }

        fun bind(item: VoteData.VoteItem) {
            if (item !is VoteData.VoteItem.Title) return
            binding.viewModel = voteViewModel
            binding.executePendingBindings()
        }
    }

    class DividerViewHolder(binding: ItemVoteDividerBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup): RecyclerView.ViewHolder {
                return DividerViewHolder(ItemVoteDividerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    class ContentViewHolder(
        private val binding: ItemVoteContentBinding,
        private val voteViewModel: VoteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, voteViewModel: VoteViewModel): RecyclerView.ViewHolder {
                return ContentViewHolder(
                    ItemVoteContentBinding.inflate(LayoutInflater.from(parent.context), parent, false), voteViewModel
                )
            }
        }

        fun bind(item: VoteData.VoteItem) {
            if (item !is VoteData.VoteItem.Content) return
            binding.viewModel = voteViewModel
            binding.content = item
            binding.viewHolder = this
            binding.executePendingBindings()
        }
    }

    class OptionEditViewHolder(
        private val binding: ItemVoteOptionEditBinding,
        private val voteViewModel: VoteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, voteViewModel: VoteViewModel): RecyclerView.ViewHolder {
                return OptionEditViewHolder(
                    ItemVoteOptionEditBinding.inflate(LayoutInflater.from(parent.context), parent, false), voteViewModel
                )
            }
        }

        fun bind(item: VoteData.VoteItem) {
            if (item !is VoteData.VoteItem.OptionEdit) return
            binding.viewModel = voteViewModel
            binding.executePendingBindings()
        }
    }

    class OptionMultipleViewHolder(
        private val binding: ItemVoteOptionMultipleBinding,
        private val voteViewModel: VoteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, voteViewModel: VoteViewModel): RecyclerView.ViewHolder {
                return OptionMultipleViewHolder(
                    ItemVoteOptionMultipleBinding.inflate(LayoutInflater.from(parent.context), parent, false), voteViewModel
                )
            }
        }

        fun bind(item: VoteData.VoteItem) {
            if (item !is VoteData.VoteItem.OptionMultiple) return
            binding.viewModel = voteViewModel
            binding.multiple = item
            binding.executePendingBindings()
        }
    }

    class OptionOverlapViewHolder(
        private val binding: ItemVoteOptionOverlapBinding,
        private val voteViewModel: VoteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun getInstance(parent: ViewGroup, voteViewModel: VoteViewModel): RecyclerView.ViewHolder {
                return OptionOverlapViewHolder(
                    ItemVoteOptionOverlapBinding.inflate(LayoutInflater.from(parent.context), parent, false), voteViewModel
                )
            }
        }

        fun bind(item: VoteData.VoteItem) {
            if (item !is VoteData.VoteItem.OptionOverlap) return
            binding.viewModel = voteViewModel
            binding.optionOverlap = item
            binding.executePendingBindings()
        }
    }

}