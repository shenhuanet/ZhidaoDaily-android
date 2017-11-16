package com.shenhua.zhidaodaily.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by Shenhua on 12/10/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public class AppUtils {

    private static AppUtils instance;
    private static final String CONFIG = "Config";
    public static final String API_TAKEN = "******";

    public synchronized static AppUtils getInstance() {
        if (instance == null) {
            instance = new AppUtils();
        }
        return instance;
    }

    private AppUtils() {
    }

    /**
     * 网络是否可用
     *
     * @param context context
     * @return true可用, false不可用
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (NetworkInfo aNetworkInfo : networkInfo) {
                    if (aNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 保存图片到sd卡
     *
     * @param context context
     * @param bitmap  bitmap
     * @return path
     * @throws Exception exception
     */
    private static String saveBitmapToSDCard(Context context, Bitmap bitmap) throws Exception {
        return saveBitmapToSDCard(context, bitmap, System.currentTimeMillis() + ".jpg", Constants.FILE_DIR, true);
    }

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
    public static String saveBitmapToSDCard(Context context, Bitmap bitmap,
                                            String title, String dirName,
                                            boolean shouldRefreshGallery) throws Exception {
        File dir = new File(Environment.getExternalStorageDirectory(), dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, title + ".jpg");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        if (bitmap == null) {
            throw new Exception("bitmap is null");
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
        if (shouldRefreshGallery) {
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + dirName + file.getAbsolutePath())));
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取路径中的文件名
     *
     * @param pathAndName apks/app.apk
     * @return app
     */
    public static String getFileName(String pathAndName) {
        int start = pathAndName.lastIndexOf("/");
        int end = pathAndName.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathAndName.substring(start + 1, end);
        } else {
            return null;
        }

    }

    /**
     * 保存网络图片
     *
     * @param context context
     * @param imgUrl  image url
     */
    public void saveUrlImg(Context context, String imgUrl) throws Exception {
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context).load(imgUrl).asBitmap()
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
        } catch (InterruptedException e) {
            Toast.makeText(context, "图片加载失败\nInterruptedException", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            Toast.makeText(context, "图片加载失败\nExecutionException", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }
        if (bitmap != null) {
            saveBitmapToSDCard(context, bitmap);
        }
    }

    public synchronized void setThemeConfig(Context context, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        preferences.edit().putBoolean("theme", value).apply();
    }

    public synchronized void setUpdateConfig(Context context, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        preferences.edit().putBoolean("update", value).apply();
    }

    public boolean getThemeConfig(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return preferences.getBoolean("theme", false);
    }

    public boolean getUpdateConfig(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return preferences.getBoolean("update", true);
    }

    public String getAppCurrentVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0";
        }
    }
}
