package `in`.co.okhttpissuetracker.network

import com.squareup.moshi.Json

/**
 * To format the provided string to MMDDYYYY format
 * val str = "2021-07-14T01:57:09Z"
 * val finalStr = "07-14-2021"
 */
fun String.formatToDate(): String {
    val year = this.substring(0, 4)
    val month = this.substring(5, 7)
    val day = this.substring(8, 10)
    return "$month-$day-$year"
}

data class Issues(
    val active_lock_reason: Any?,
    val assignee: Any?,
    val assignees: List<Any>?,
    val author_association: String,
    val body: String?,
    val closed_at: Any?,
    val comments: Long,
    val comments_url: String,
    val created_at: String,
    val events_url: String,
    val html_url: String,
    val id: Long,
    val labels: List<Label>?,
    val labels_url: String,
    val locked: Boolean,
    val milestone: Milestone?,
    val node_id: String,
    val number: Long,
    val performed_via_github_app: Any?,
    val pull_request: PullRequest,
    val reactions: Reactions,
    val repository_url: String,
    val state: String,
    val timeline_url: String,
    val title: String,
    val updated_at: String,
    val url: String,
    val user: User
)

data class Comments(
    val author_association: String,
    val body: String,
    val created_at: String,
    val html_url: String,
    val id: Long,
    val issue_url: String,
    val node_id: String,
    val performed_via_github_app: Any,
    val reactions: Reactions,
    val updated_at: String,
    val url: String,
    val user: User
)

data class Creator(
    val avatar_url: String,
    val events_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val gravatar_id: String,
    val html_url: String,
    val id: Long,
    val login: String,
    val node_id: String,
    val organizations_url: String,
    val received_events_url: String,
    val repos_url: String,
    val site_admin: Boolean,
    val starred_url: String,
    val subscriptions_url: String,
    val type: String,
    val url: String
)

data class Label(
    val color: String,
    val default: Boolean,
    val description: String,
    val id: Double,
    val name: String,
    val node_id: String,
    val url: String
)

data class Milestone(
    val closed_at: String?,
    val closed_issues: Long,
    val created_at: String,
    val creator: Creator,
    val description: String,
    val due_on: String?,
    val html_url: String,
    val id: Long,
    val labels_url: String,
    val node_id: String,
    val number: Long,
    val open_issues: Long,
    val state: String,
    val title: String,
    val updated_at: String,
    val url: String
)

data class PullRequest(
    val diff_url: String,
    val html_url: String,
    val patch_url: String,
    val url: String
)

data class Reactions(
    @Json(name = "+1")
    val plus_one: Long,
    @Json(name = "-1")
    val minus_one: Long,
    val confused: Long,
    val eyes: Long,
    val heart: Long,
    val hooray: Long,
    val laugh: Long,
    val rocket: Long,
    val total_count: Long,
    val url: String
)

data class User(
    val avatar_url: String,
    val events_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val gravatar_id: String,
    val html_url: String,
    val id: Long,
    val login: String,
    val node_id: String,
    val organizations_url: String,
    val received_events_url: String,
    val repos_url: String,
    val site_admin: Boolean,
    val starred_url: String,
    val subscriptions_url: String,
    val type: String,
    val url: String
)