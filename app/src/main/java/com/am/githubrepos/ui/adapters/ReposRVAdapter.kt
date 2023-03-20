package com.am.githubrepos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.am.githubrepos.data.model.GithubRepository
import com.am.githubrepos.databinding.GithubRepoItemBinding
import java.text.SimpleDateFormat

class ReposRVAdapter : PagingDataAdapter<GithubRepository, ReposRVAdapter.ReposViewHolder>(
    REPO_COMPARATOR
) {
    private lateinit var mListener: OnItemClickedListener
    private var parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    private var formatter = SimpleDateFormat("dd/MM/yyyy h:mm a")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        return ReposViewHolder(
            GithubRepoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class ReposViewHolder(
        var binding: GithubRepoItemBinding,
        listener: OnItemClickedListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val item = getItem(absoluteAdapterPosition)
                if (item != null) {
                    listener.onItemClick(item.fullName)
                }
            }
        }

        fun bind(githubRepository: GithubRepository) {
            binding.apply {
                tvName.text = githubRepository.name
                val creationDate = parseCreationDate(githubRepository.creationDate)
                tvCreationDate.text = creationDate
                imgUserProfile.load(githubRepository.owner.avatarUrl) {
                    crossfade(true)
                    crossfade(1000)
                    transformations(RoundedCornersTransformation(10F))
                }
            }
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<GithubRepository>() {
            override fun areItemsTheSame(
                oldItem: GithubRepository,
                newItem: GithubRepository
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: GithubRepository,
                newItem: GithubRepository
            ): Boolean = oldItem == newItem
        }
    }

    interface OnItemClickedListener {
        fun onItemClick(fullName: String)
    }

    fun setOnItemClickedListener(listener: OnItemClickedListener) {
        mListener = listener
    }

    private fun parseCreationDate(date: String): String {
        return formatter.format(parser.parse(date))
    }
}