package `in`.co.okhttpissuetracker

import android.app.Application
import androidx.viewbinding.BuildConfig
import timber.log.Timber

class OkHttpIssueTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}