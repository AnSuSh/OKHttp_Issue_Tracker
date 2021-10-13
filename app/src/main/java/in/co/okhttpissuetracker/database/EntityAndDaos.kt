package `in`.co.okhttpissuetracker.database

import `in`.co.okhttpissuetracker.network.formatToDate
import `in`.co.okhttpissuetracker.repository.CommentAppModel
import `in`.co.okhttpissuetracker.repository.IssueAppModel
import androidx.core.net.toUri
import androidx.room.*

@Entity(tableName = "issues_table")
data class IssueTable(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "issue_id")
    val issueId: Long,
    @ColumnInfo(name = "issue_title")
    val issueTitle: String,
    @ColumnInfo(name = "issue_description")
    val issueDescription: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "user_avatar_url")
    val avatarUrl: String,
    @ColumnInfo(name = "update_time")
    val updatedTime: String
)

@Entity(tableName = "comments_table")
data class CommentsTable(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "comment_id")
    val commentId: Long,
    @ColumnInfo(name = "issue_id")
    val issueId: Long,
    @ColumnInfo(name = "comment_body")
    val commentBody: String,
    @ColumnInfo(name = "username")
    val username: String,
    @ColumnInfo(name = "update_time")
    val updatedTime: String
)

@Dao
interface IssuesDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllIssues(issues: List<IssueTable>)

    @Transaction
    @Query("SELECT * FROM issues_table ORDER BY issue_id DESC")
    fun getAllIssues(): List<IssueTable>?

    @Query("SELECT * FROM issues_table WHERE issue_id=:issueId")
    fun getIssue(issueId: Long): IssueTable?
}

@Dao
interface CommentsDao {
    @Transaction
    @Query("SELECT * FROM comments_table WHERE issue_id=:issueId")
    fun getComments(issueId: Long): List<CommentsTable>?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllComments(comments: List<CommentsTable>)
}

@JvmName("IssueTableAsAppModel")
fun List<IssueTable>.asAppModel(): List<IssueAppModel> {
    return map {
        IssueAppModel(
            issueId = it.issueId,
            issueTitle = it.issueTitle,
            issueDescription = it.issueDescription,
            username = it.username,
            avatarUrl = it.avatarUrl.toUri().buildUpon().scheme("https").build(),
            updatedTime = it.updatedTime.formatToDate()
        )
    }
}

fun IssueTable.asAppModel(): IssueAppModel {
    return IssueAppModel(
        issueId = this.issueId,
        issueTitle = this.issueTitle,
        issueDescription = this.issueDescription,
        username = this.username,
        avatarUrl = this.avatarUrl.toUri().buildUpon().scheme("https").build(),
        updatedTime = this.updatedTime.formatToDate()
    )
}

@JvmName("CommentsTableAsAppModel")
fun List<CommentsTable>.asAppModel(): List<CommentAppModel> {
    return map {
        CommentAppModel(
            commentId = it.commentId,
            issueId = it.issueId,
            commentBody = it.commentBody,
            username = it.username,
            updatedTime = it.updatedTime.formatToDate()
        )
    }
}
