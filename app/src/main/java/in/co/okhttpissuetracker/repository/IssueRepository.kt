package `in`.co.okhttpissuetracker.repository

import `in`.co.okhttpissuetracker.database.IssueDatabase
import `in`.co.okhttpissuetracker.database.asAppModel
import `in`.co.okhttpissuetracker.network.CommentAPIService
import `in`.co.okhttpissuetracker.network.IssueApi
import `in`.co.okhttpissuetracker.network.asDatabaseModel
import android.net.Uri
import kotlinx.coroutines.InternalCoroutinesApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

@InternalCoroutinesApi
class IssueRepository(private val database: IssueDatabase, private val issueId: Long = 0L) {

    val issues: List<IssueAppModel> = database.issueDao.getAllIssues()?.asAppModel() ?: ArrayList()
    val comments: List<CommentAppModel> =
        database.commentsDao.getComments(issueId)?.asAppModel() ?: ArrayList()
    val issue: IssueAppModel = database.issueDao.getIssue(issueId)?.asAppModel() ?: IssueAppModel(
        0L, "", "", "", Uri.EMPTY, ""
    )
    private val baseUrl = "https://api.github.com"

    suspend fun refreshIssues() {
        val issues = IssueApi.service.getIssuesFromServer()
        Timber.d("Connecting to server.. return is issues which is null? ${issues.isNullOrEmpty()}")
        Timber.d("${issues?.size}")
        if (issues != null) {
            database.issueDao.insertAllIssues(issues.asDatabaseModel())
        }
        Timber.d("Database insertion successful!!")
    }

    private fun getFinalUrl(): String {
        return "$baseUrl/repos/square/okhttp/issues/$issueId/"
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(getFinalUrl())
        .build()

    suspend fun refreshComments() {
        val service: CommentAPIService by lazy {
            retrofit.create(CommentAPIService::class.java)
        }
        val comments = service.getCommentsFromServer()
        Timber.d("${comments?.size}")
        // TODO: 13/10/21 Check if comments list is empty or not. If empty show user 'No comments'
        if (comments != null) {
            database.commentsDao.insertAllComments(comments.asDatabaseModel(issueId))
        }
        Timber.d("Database insertion successful!!")
    }
}

data class IssueAppModel(
    val issueId: Long,
    val issueTitle: String,
    val issueDescription: String,
    val username: String,
    val avatarUrl: Uri,
    val updatedTime: String
)

data class CommentAppModel(
    val commentId: Long,
    val issueId: Long,
    val commentBody: String,
    val username: String,
    val updatedTime: String
)