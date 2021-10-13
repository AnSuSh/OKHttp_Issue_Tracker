package `in`.co.okhttpissuetracker.ui.detail

import `in`.co.okhttpissuetracker.database.IssueDatabase
import `in`.co.okhttpissuetracker.network.IssueApiStatus
import `in`.co.okhttpissuetracker.repository.CommentAppModel
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
class IssueDetailViewModel(application: Application, issueId: Long) :
    AndroidViewModel(application) {

    private lateinit var issueRepo: IssueRepository

    private var _issue = MutableLiveData<IssueAppModel>()
    val issue: LiveData<IssueAppModel> = _issue

    private var _comments = MutableLiveData<List<CommentAppModel>>()
    val comments: LiveData<List<CommentAppModel>> = _comments

    private val _status = MutableLiveData(IssueApiStatus.LOADING)
    val status: LiveData<IssueApiStatus> = _status

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                issueRepo = IssueRepository(IssueDatabase.getInstance(application), issueId)
                _comments.postValue(issueRepo.comments)
                _issue.postValue(issueRepo.issue)
            }
        }
    }

    fun refreshRepository() {
        _status.value = IssueApiStatus.LOADING
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    issueRepo.refreshComments()
                } catch (ex: IOException) {
                    _status.postValue(IssueApiStatus.ERROR)
                    Timber.d("Error: ${ex.localizedMessage}")
                }
                refreshDatabase()
                Timber.d("${issueRepo.comments.size} courses loaded from the repository!!")
            }
        }
    }

    private fun refreshDatabase() {
        val repo = IssueRepository(IssueDatabase.getInstance(getApplication()))
        _comments.postValue(repo.comments)
    }

    fun updateStatus(status: IssueApiStatus) {
        _status.value = status
    }
}

@Suppress("UNCHECKED_CAST")
@InternalCoroutinesApi
class DetailViewModelFactory(private val application: Application, private val issueId: Long) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IssueDetailViewModel::class.java))
            return IssueDetailViewModel(application, issueId) as T
        throw IllegalArgumentException("MainViewModel class not exist!!")
    }
}