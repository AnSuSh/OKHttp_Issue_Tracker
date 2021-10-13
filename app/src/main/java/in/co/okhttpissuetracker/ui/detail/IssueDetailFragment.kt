package `in`.co.okhttpissuetracker.ui.detail

import `in`.co.okhttpissuetracker.R
import `in`.co.okhttpissuetracker.databinding.IssueDetailFragmentBinding
import `in`.co.okhttpissuetracker.network.IssueApiStatus
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber

@InternalCoroutinesApi
class IssueDetailFragment : Fragment() {

    private lateinit var viewModel: IssueDetailViewModel
    private lateinit var issueBinding: IssueDetailFragmentBinding
    private lateinit var args: IssueDetailFragmentArgs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        issueBinding =
            DataBindingUtil.inflate(inflater, R.layout.issue_detail_fragment, container, false)
        setHasOptionsMenu(true)
        return issueBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args = IssueDetailFragmentArgs.fromBundle(requireArguments())
        Timber.d("Argument data: ${args.issueId}")
        val application = requireActivity().application
        val viewModelFactory = DetailViewModelFactory(application, args.issueId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(IssueDetailViewModel::class.java)

        val adapter = CommentAdapter()

        viewModel.comments.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
//                Toast.makeText(
//                    this.context,
//                    "No Courses available at the moment!!, Please Refresh!!",
//                    Toast.LENGTH_LONG
//                ).show()
                Timber.d("No comments in database!!, Waiting")
                viewModel.updateStatus(IssueApiStatus.ERROR)
            } else {
                Timber.d("Comments available from database!!")
                viewModel.updateStatus(IssueApiStatus.DONE)
            }
        })

        val decor =
            DividerItemDecoration(issueBinding.commentsRecyclerView.context, RecyclerView.VERTICAL)
        issueBinding.apply {
            commentsRecyclerView.adapter = adapter
            commentsRecyclerView.addItemDecoration(decor)
            lifecycleOwner = viewLifecycleOwner
            model = viewModel
            executePendingBindings()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.issue_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.refresh_repository) {
            viewModel.refreshRepository()
        }
        return true
    }
}