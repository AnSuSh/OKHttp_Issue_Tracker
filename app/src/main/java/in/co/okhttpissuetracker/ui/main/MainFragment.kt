package `in`.co.okhttpissuetracker.ui.main

import `in`.co.okhttpissuetracker.R
import `in`.co.okhttpissuetracker.databinding.MainFragmentBinding
import `in`.co.okhttpissuetracker.network.IssueApiStatus
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber

@InternalCoroutinesApi
class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var mainBinding: MainFragmentBinding
    private lateinit var adapter: IssueAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireActivity().application
        val viewModelFactory = MainViewModelFactory(application)
        viewModel =
            ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        mainBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        adapter = IssueAdapter(IssueClickListener {
            viewModel.onIssueItemClicked(it)
        })

//        Navigation to detail fragment
        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToIssueDetailFragment(it)
                )
                Timber.d("Navigated to detail View")
                viewModel.onIssueDetailNavigated()
            }
        })

        viewModel.issues.observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
//                Toast.makeText(
//                    this.context,
//                    "No Courses available at the moment!!, Please Refresh!!",
//                    Toast.LENGTH_LONG
//                ).show()
                Timber.d("No issues in database!!, Waiting")
                viewModel.updateStatus(IssueApiStatus.ERROR)
            } else {
                Timber.d("Issues available from database!!")
                viewModel.updateStatus(IssueApiStatus.DONE)
            }
        })

        val decor =
            DividerItemDecoration(mainBinding.issuesRecyclerView.context, RecyclerView.VERTICAL)

        mainBinding.issuesRecyclerView.adapter = adapter
        mainBinding.model = viewModel
        mainBinding.lifecycleOwner = viewLifecycleOwner
        mainBinding.issuesRecyclerView.addItemDecoration(decor)
        setHasOptionsMenu(true)
        return mainBinding.root
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