package com.shenhua.zhidaodaily

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.shenhua.zhidaodaily.utils.Utils
import com.tencent.smtt.sdk.QbSdk

/**
 * Created by shenhua on 2017-11-16-0016.
 *
 * @author shenhua
 * Email shenhuanet@126.com
 */
class App : Application() {


    override fun onCreate() {
        super.onCreate()
        val dayNightTheme = Utils.dayNightTheme(this)
        Utils.dayNightTheme = dayNightTheme
        AppCompatDelegate.setDefaultNightMode(if (dayNightTheme) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
        val cb = object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
            }

            override fun onCoreInitFinished() {}
        }
        QbSdk.initX5Environment(applicationContext, cb)
    }
}
