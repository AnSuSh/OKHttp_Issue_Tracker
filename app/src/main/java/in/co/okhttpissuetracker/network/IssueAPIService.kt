package `in`.co.okhttpissuetracker.network

import `in`.co.okhttpissuetracker.database.IssueTable
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.github.com"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

/**
 * Api service for communication with remote server.
 * */
object IssueApi {
    val service: IssueAPIService by lazy {
        retrofit.create(IssueAPIService::class.java)
    }
}

enum class IssueApiStatus {
    LOADING, ERROR, DONE
}

interface IssueAPIService {
    @GET("repos/square/okhttp/issues")
    suspend fun getIssuesFromServer(): List<Issues>?
}

fun List<Issues>.asDatabaseModel(): List<IssueTable>{
    return map {
        IssueTable(
            issueId = it.number,
            issueTitle = it.title,
            issueDescription = it.body?: "",
            username = it.user.login,
            avatarUrl = it.user.avatar_url,
            updatedTime = it.updated_at
        )
    }
}