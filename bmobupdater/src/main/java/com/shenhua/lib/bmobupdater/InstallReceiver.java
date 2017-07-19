package com.shenhua.lib.bmobupdater;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by shenhua on 2017-06-21.
 * Email shenhuanet@126.com
 */
public class InstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            long cacheDownLoadId = PreferenceManager.getDefaultSharedPreferences(context).getLong("download_apk_id_prefs", -1L);
            if (cacheDownLoadId == downLoadId) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                File apkFile = queryDownloadedApk(context);
                install.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            }
        }
    }

    private File queryDownloadedApk(Context context) {
        File targerApkFile = null;
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = PreferenceManager.getDefaultSharedPreferences(context).getLong("download_apk_id_prefs", -1L);
        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cursor = downloadManager.query(query);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targerApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
            }
        }
        downloadManager.remove(downloadId);
        return targerApkFile;
    }
}
