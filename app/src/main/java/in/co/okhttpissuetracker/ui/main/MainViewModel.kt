package `in`.co.okhttpissuetracker.ui.main

import `in`.co.okhttpissuetracker.database.IssueDatabase
import `in`.co.okhttpissuetracker.network.IssueApiStatus
import `in`.co.okhttpissuetracker.repository.IssueAppModel
import `in`.co.okhttpissuetracker.repository.IssueRepository
import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

@InternalCoroutinesApi
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var issueRepo: IssueRepository

    private var _issues = MutableLiveData<List<IssueAppModel>>()
    val issues: LiveData<List<IssueAppModel>> = _issues

    private val _status = MutableLiveData(IssueApiStatus.LOADING)
    val status: LiveData<IssueApiStatus> = _status

    private val _navigateToDetailFragment = MutableLiveData<Long?>()
    val navigateToDetailFragment = _navigateToDetailFragment

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                issueRepo = IssueRepository(IssueDatabase.getInstance(application))
                _issues.postValue(issueRepo.issues)
            }
        }
    }

    fun onIssueItemClicked(issueId: Long) {
        _navigateToDetailFragment.value = issueId
    }

    fun onIssueDetailNavigated() {
        _navigateToDetailFragment.value = null
    }

    fun refreshRepository() {
        _status.value = IssueApiStatus.LOADING
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    issueRepo.refreshIssues()
                } catch (ex: IOException) {
                    _status.postValue(IssueApiStatus.ERROR)
                    Timber.d("Error: ${ex.localizedMessage}")
                }
                refreshDatabase()
                Timber.d("${issueRepo.issues.size} courses loaded from the repository!!")
            }
        }
    }

    private fun refreshDatabase() {
        val repo = IssueRepository(IssueDatabase.getInstance(getApplication()))
        _issues.postValue(repo.issues)
    }

    fun updateStatus(status: IssueApiStatus) {
        _status.value = status
    }
}

@Suppress("UNCHECKED_CAST")
@InternalCoroutinesApi
class MainViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            return MainViewModel(application) as T
        throw IllegalArgumentException("MainViewModel class not exist!!")
    }
}