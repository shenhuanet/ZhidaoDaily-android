package com.shenhua.zhidaodaily.utils

import android.content.Context
import android.text.format.Formatter

import java.io.File

/**
 * 文件数据操作类
 * Created by shenhua on 7/15/2016.
 *
 * @author shenhua
 */
object DataCleanManager {
    /**
     * 获取缓存大小
     *
     * @param context context
     * @return 0KB/23MB
     */
    fun getCacheSize(context: Context): String {
        val cache = context.cacheDir
        val exCache = context.externalCacheDir
        val size = getFolderSize(cache) + getFolderSize(exCache)
        return Formatter.formatFileSize(context, size)
    }

    /**
     * 清理缓存
     *
     * @param context context
     */
    fun cleanCache(context: Context) {
        val cache = context.cacheDir
        val exCache = context.externalCacheDir
        deleteDir(cache)
        deleteDir(exCache)
    }

    private fun getFolderSize(file: File?): Long {
        var size: Long = 0
        try {
            val files = file!!.listFiles()
            for (file1 in files) {
                size = if (file1.isDirectory) {
                    size + getFolderSize(file1)
                } else {
                    size + file1.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return size
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir == null) {
            return true
        }
        if (dir.isDirectory) {
            val list = dir.list()
            for (s in list) {
                deleteDir(File(dir, s))
            }
        }
        return dir.delete()
    }
}
