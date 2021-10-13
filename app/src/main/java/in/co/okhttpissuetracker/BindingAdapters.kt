package `in`.co.okhttpissuetracker

import `in`.co.okhttpissuetracker.network.IssueApiStatus
import `in`.co.okhttpissuetracker.repository.CommentAppModel
import `in`.co.okhttpissuetracker.repository.IssueAppModel
import `in`.co.okhttpissuetracker.ui.detail.CommentAdapter
import `in`.co.okhttpissuetracker.ui.main.IssueAdapter
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.progressindicator.CircularProgressIndicator
import timber.log.Timber

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, imageUrl: Uri?) {
    imageUrl?.let {
        Glide.with(imageView.context)
            .load(imageUrl)
            .into(imageView)
    }
}

@BindingAdapter("listIssues")
fun bindIssues(recyclerView: RecyclerView, issues: List<IssueAppModel>?) {
    val adapter = recyclerView.adapter as? IssueAdapter
    adapter?.submitList(issues)
}

@BindingAdapter("listComments")
fun bindComments(recyclerView: RecyclerView, comments: List<CommentAppModel>?) {
    val adapter = recyclerView.adapter as? CommentAdapter
    adapter?.submitList(comments)
}

// Animation view loading state
@BindingAdapter("progressBarStatus")
fun bindStatusImageView(progressView: CircularProgressIndicator, status: IssueApiStatus?) {
    when (status) {
        IssueApiStatus.LOADING -> {
            Timber.d("Status: Loading, showing Progressbar")
            progressView.visibility = View.VISIBLE
        }
        IssueApiStatus.ERROR -> {
            progressView.visibility = View.GONE
        }
        IssueApiStatus.DONE -> {
            progressView.visibility = View.GONE
        }
    }
}

@BindingAdapter("recyclerViewStatus")
fun bindStatusRecyclerView(recyclerView: RecyclerView, status: IssueApiStatus?) {
    when (status) {
        IssueApiStatus.LOADING -> {
            recyclerView.visibility = View.INVISIBLE
        }
        IssueApiStatus.ERROR -> {
            recyclerView.visibility = View.INVISIBLE
        }
        IssueApiStatus.DONE -> {
            Timber.d("Status: Done, showing RecyclerView")
            recyclerView.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("imageStatus")
fun imageStatus(imageView: ImageView, status: IssueApiStatus?) {
    when (status) {
        IssueApiStatus.LOADING -> {
            imageView.visibility = View.GONE
        }
        IssueApiStatus.ERROR -> {
            Timber.d("Status: Error, showing ImageView")
            imageView.visibility = View.VISIBLE
            imageView.setImageResource(R.drawable.ic_connection_error)
        }
        IssueApiStatus.DONE -> {
            imageView.visibility = View.GONE
        }
    }
}
