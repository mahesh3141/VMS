package com.airport.vms.applications

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import com.airport.vms.data.utils.AppSharedPreferences
import com.airport.vms.data.utils.LocaleHelper


val prefs: AppSharedPreferences by lazy {
    VMS.prefs!!
}
class VMS : Application(), Application.ActivityLifecycleCallbacks {
    var currentActivity: Activity? = null

    companion object {
        lateinit var ctx: VMS
            private set
        var prefs: AppSharedPreferences? = null
    }

    override fun onCreate() {
        super.onCreate()

        ctx = this
        prefs = AppSharedPreferences()
        registerActivityLifecycleCallbacks(this)
        /**Use below to debug issue of TransactionTooLargeException*/
//        TooLargeTool.startLogging(this)
    }

    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity?) {
        currentActivity = activity
    }

    override fun onActivityDestroyed(activity: Activity?) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity?) {
        if (activity == currentActivity) {
            currentActivity = null
        }
    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base?.let { LocaleHelper.onAttach(it) } ?: base)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.setLocale(baseContext, prefs?.languageCode)
    }
}