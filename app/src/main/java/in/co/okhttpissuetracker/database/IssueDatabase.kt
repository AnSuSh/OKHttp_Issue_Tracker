package `in`.co.okhttpissuetracker.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@Database(entities = [IssueTable::class, CommentsTable::class], version = 1)
abstract class IssueDatabase : RoomDatabase() {
    abstract val issueDao: IssuesDao
    abstract val commentsDao: CommentsDao

    companion object {
        @Volatile
        private var INSTANCE: IssueDatabase? = null

        fun getInstance(context: Context): IssueDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context,
                        IssueDatabase::class.java,
                        "issue_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}