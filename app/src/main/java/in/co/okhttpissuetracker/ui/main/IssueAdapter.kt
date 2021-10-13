package `in`.co.okhttpissuetracker.ui.main

import `in`.co.okhttpissuetracker.databinding.IssueListItemBinding
import `in`.co.okhttpissuetracker.repository.IssueAppModel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class IssueAdapter(private val clickListener: IssueClickListener) :
    ListAdapter<IssueAppModel, IssueViewHolder>(IssueDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        return IssueViewHolder.from(parent)
    }

    // Implemented in ListAdapter itself
//    override fun getItemCount(): Int {
//        return this.dataset.size
//    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        val item = getItem(position)
        Timber.d("Binding View with item ${item.issueTitle}")
        holder.bind(item, clickListener)
    }
}

class IssueViewHolder private constructor(
    private val binding: IssueListItemBinding
) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(
            parent: ViewGroup
        ): IssueViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = IssueListItemBinding.inflate(layoutInflater, parent, false)
            return IssueViewHolder(binding)
        }
    }

    fun bind(item: IssueAppModel, clickListener: IssueClickListener) {
        binding.issue = item
        binding.issueClick = clickListener
        binding.executePendingBindings()
    }
}

class IssueDiffCallback : DiffUtil.ItemCallback<IssueAppModel>() {
    override fun areItemsTheSame(oldItem: IssueAppModel, newItem: IssueAppModel): Boolean {
        return oldItem.issueId == newItem.issueId
    }

    override fun areContentsTheSame(oldItem: IssueAppModel, newItem: IssueAppModel): Boolean {
        return oldItem == newItem
    }
}

class IssueClickListener(val clickListener: (issueId: Long) -> Unit) {
    fun onClick(issue: IssueAppModel) {
        Timber.d("Performing Click in cardview")
        clickListener(issue.issueId)
    }
}