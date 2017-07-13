package com.shenhua.zhidaodaily.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * Created by shenhua on 2017-06-21.
 * Email shenhuanet@126.com
 */
public class InstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadApkId != -1) {
                installApk(context, downloadApkId);
            }
        }
    }

    private void installApk(Context context, long downloadApkId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        long id = sp.getLong("download_apk_id_prefs", -1L);
        if (downloadApkId == id) {
            DownloadManager dManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Intent install = new Intent(Intent.ACTION_VIEW);
            Uri downloadFileUri = dManager.getUriForDownloadedFile(downloadApkId);
            if (downloadFileUri != null) {
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            } else {
                Toast.makeText(context, context.getPackageManager().getApplicationLabel(
                        context.getApplicationInfo())+"新版本apk下载失败", Toast.LENGTH_SHORT).show();
            }
            dManager.remove(downloadApkId);
        }
    }
}
