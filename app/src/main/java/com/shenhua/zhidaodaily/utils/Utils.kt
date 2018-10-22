package com.shenhua.zhidaodaily.utils

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.Window

/**
 * Created by shenhua on 2018/10/22.
 *
 * @author shenhua
 * Email shenhuanet@126.com
 */
object Utils {

    /**
     * 夜间模式
     */
    var dayNightTheme = true

    fun dayNightTheme(context: Context, dayNight: Boolean) {
        val preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        preferences.edit().putBoolean("isDayNightTheme", dayNight).apply()
    }

    fun dayNightTheme(context: Context): Boolean {
        val preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        return preferences.getBoolean("isDayNightTheme", false)
    }

    fun autoUpdate(context: Context, autoUpdate: Boolean) {
        val preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        preferences.edit().putBoolean("isAutoUpdate", autoUpdate).apply()
    }

    fun autoUpdate(context: Context): Boolean {
        val preferences = context.getSharedPreferences("config", Context.MODE_PRIVATE)
        return preferences.getBoolean("isAutoUpdate", true)
    }

    /**
     * 分享纯文本
     *
     * @param context context
     * @param text    文本内容
     */
    fun shareText(context: Context, text: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, text)
        context.startActivity(Intent.createChooser(intent, "来自知道日报"))
    }

    /**
     * 分享图片
     *
     * @param context context
     * @param path    图片路径
     */
    fun shareImage(context: Context, path: String) {
        val imageIntent = Intent(Intent.ACTION_SEND)
        imageIntent.type = "image/jpeg"
        imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
        context.startActivity(Intent.createChooser(imageIntent, "来自知道日报"))
    }

    fun darkStatusBarIcon(window: Window?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dayNightTheme) {
                window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            } else {
                window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window?.statusBarColor = Color.TRANSPARENT
        }
    }

    fun liteStatusBarIcon(window: Window?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dayNightTheme) {
                window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
            window?.statusBarColor = Color.TRANSPARENT
        }
    }
}
