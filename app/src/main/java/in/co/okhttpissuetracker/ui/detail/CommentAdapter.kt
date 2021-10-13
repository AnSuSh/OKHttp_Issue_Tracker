package `in`.co.okhttpissuetracker.ui.detail

import `in`.co.okhttpissuetracker.databinding.CommentListItemBinding
import `in`.co.okhttpissuetracker.repository.CommentAppModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CommentAdapter : ListAdapter<CommentAppModel, CommentViewHolder>(CommentDiffCllback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class CommentViewHolder private constructor(
    private val binding: CommentListItemBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup
        ): CommentViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = CommentListItemBinding.inflate(layoutInflater, parent, false)
            return CommentViewHolder(binding)
        }
    }

    fun bind(item: CommentAppModel) {
        binding.comment = item
        binding.executePendingBindings()
    }
}

class CommentDiffCllback : DiffUtil.ItemCallback<CommentAppModel>() {
    override fun areItemsTheSame(oldItem: CommentAppModel, newItem: CommentAppModel): Boolean {
        return oldItem.commentId == newItem.commentId
    }

    override fun areContentsTheSame(oldItem: CommentAppModel, newItem: CommentAppModel): Boolean {
        return oldItem == newItem
    }
}
