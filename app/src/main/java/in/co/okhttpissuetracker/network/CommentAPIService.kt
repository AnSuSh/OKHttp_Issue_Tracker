package `in`.co.okhttpissuetracker.network

import `in`.co.okhttpissuetracker.database.CommentsTable
import retrofit2.http.GET

/**
 * Api service for communication with remote server.
 * */
interface CommentAPIService {
    @GET("comments")
    suspend fun getCommentsFromServer(): List<Comments>?
}

fun List<Comments>.asDatabaseModel(issueId: Long): List<CommentsTable> {
    return map {
        CommentsTable(
            commentId = it.id,
            issueId = issueId,
            commentBody = it.body,
            username = it.user.login,
            updatedTime = it.updated_at
        )
    }
}