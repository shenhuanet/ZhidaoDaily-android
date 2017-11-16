package com.shenhua.zhidaodaily.utils;

import android.content.Context;
import android.text.format.Formatter;

import java.io.File;

/**
 * 文件数据操作类
 * Created by shenhua on 7/15/2016.
 *
 * @author shenhua
 */
public class DataCleanManager {
    /**
     * 获取缓存大小
     *
     * @param context context
     * @return 0KB/23MB
     */
    public static String getCacheSize(Context context) {
        File cache = context.getCacheDir();
        File exCache = context.getExternalCacheDir();
        long size = getFolderSize(cache) + getFolderSize(exCache);
        return Formatter.formatFileSize(context, size);
    }

    /**
     * 清理缓存
     *
     * @param context context
     */
    public static void cleanCache(Context context) {
        File cache = context.getCacheDir();
        File exCache = context.getExternalCacheDir();
        deleteDir(cache);
        deleteDir(exCache);
    }

    private static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] files = file.listFiles();
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    size = size + getFolderSize(file1);
                } else {
                    size = size + file1.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    private static boolean deleteDir(File dir) {
        if (dir == null) {
            return true;
        }
        if (dir.isDirectory()) {
            String[] list = dir.list();
            for (String s : list) {
                deleteDir(new File(dir, s));
            }
        }
        return dir.delete();
    }
}
