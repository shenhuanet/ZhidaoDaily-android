package com.shenhua.zhidaodaily.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream

/**
 * Created by Shenhua on 12/10/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
class AppUtils private constructor() {

    /**
     * 保存图片到手机存储
     *
     * @param context              上下文
     * @param bitmap               bitmap对象
     * @param title                文件名
     * @param dirName              文件夹名称
     * @param shouldRefreshGallery 是否刷新图库
     * @return 返回保存成功后的绝对路径
     * @throws Exception IO操作异常
     */
    @Throws(Exception::class)
    @JvmOverloads
    fun saveBitmapToSDCard(context: Context, bitmap: Bitmap?, title: String = System.currentTimeMillis().toString(), dirName: String = Constants.FILE_DIR,
                           shouldRefreshGallery: Boolean = true): String {
        val dir = File(Environment.getExternalStorageDirectory(), dirName)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dir, "$title.jpg")
        if (!file.exists()) {
            file.createNewFile()
        }
        val fileOutputStream = FileOutputStream(file)
        if (bitmap == null) {
            throw Exception("bitmap is null")
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        if (shouldRefreshGallery) {
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + dirName + file.absolutePath)))
        }
        return file.absolutePath
    }

    @Synchronized
    fun setThemeConfig(context: Context, value: Boolean) {
        val preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE)
        preferences.edit().putBoolean("theme", value).apply()
    }

    @Synchronized
    fun setUpdateConfig(context: Context, value: Boolean) {
        val preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE)
        preferences.edit().putBoolean("update", value).apply()
    }

    fun getThemeConfig(context: Context): Boolean {
        val preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE)
        return preferences.getBoolean("theme", false)
    }

    fun getUpdateConfig(context: Context): Boolean {
        val preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE)
        return preferences.getBoolean("update", true)
    }

    fun getAppCurrentVersion(context: Context): String {
        val manager = context.packageManager
        try {
            val packageInfo = manager.getPackageInfo(context.packageName, 0)
            return packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            return "1.0"
        }

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

    companion object {

        private var instance: AppUtils? = null
        private val CONFIG = "Config"
        val API_TAKEN = "******"

        @Synchronized
        fun getInstance(): AppUtils {
            if (instance == null) {
                instance = AppUtils()
            }
            return instance!!
        }

        /**
         * 获取路径中的文件名
         *
         * @param pathAndName apks/app.apk
         * @return app
         */
        fun getFileName(pathAndName: String): String? {
            val start = pathAndName.lastIndexOf("/")
            val end = pathAndName.lastIndexOf(".")
            return if (start != -1 && end != -1) {
                pathAndName.substring(start + 1, end)
            } else {
                null
            }

        }
    }


}
/**
 * 保存图片到sd卡
 *
 * @param context context
 * @param bitmap  bitmap
 * @return path
 * @throws Exception exception
 */