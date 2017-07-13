package com.shenhua.zhidaodaily.update;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.shenhua.zhidaodaily.R;

import org.json.JSONObject;

import cn.bmob.v3.AsyncCustomEndpoints;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CloudCodeListener;
import cn.bmob.v3.update.AppVersion;

/**
 * Created by shenhua on 2017-06-20.
 * Email shenhuanet@126.com
 */
public class BmobUpdateUtil {

    private Context context;
    private String fileDir = "updateFile";

    public static BmobUpdateUtil getInstance(Context context) {
        return new BmobUpdateUtil(context);
    }

    private BmobUpdateUtil(Context context) {
        this.context = context;
    }

    /**
     * 是否仅wifi更新，用于设置
     *
     * @param b true为是
     */
    public void setUpdateOnlyWifi(boolean b) {
        new Config().setUpdateOnlyWifi(b);
    }

    public void updateManual() {
        update(true);
    }

    public void updateAuto() {
        update(false);
    }

    private void update(final boolean b/*是否手动更新*/) {
        if (b) {
            Toast.makeText(context, "正在检测更新...", Toast.LENGTH_SHORT).show();
        }
        AsyncCustomEndpoints ace = new AsyncCustomEndpoints();
        // 第一个参数是云端逻辑的方法名称,(可选)第二个参数是上传到云端逻辑的参数列表(JSONObject cloudCodeParams),第三个参数是回调类
        ace.callEndpoint("update", new CloudCodeListener() {
            @Override
            public void done(Object object, BmobException e) {
                if (e != null) {
                    return;
                }
                try {
                    Log.d("shenhuaLog -- " + BmobUpdateUtil.class.getSimpleName(), "done: " + object.toString());
                    JSONObject json = new JSONObject(object.toString()).getJSONArray("results").getJSONObject(0);
                    AppVersion appversion = new AppVersion();
                    appversion.setVersion_i(json.getInt("version_i"));
                    appversion.setVersion(json.getString("version"));
                    appversion.setUpdate_log(json.getString("update_log"));
                    appversion.setChannel(json.getString("channel"));
                    appversion.setTarget_size(json.getString("target_size"));
                    appversion.setPlatform(json.getString("platform"));
                    appversion.setIsforce(json.getBoolean("isforce"));
                    appversion.setAndroid_url(json.getString("android_url"));
                    BmobFile file = BmobFile.createEmptyFile();
                    file.setUrl(new JSONObject(json.getString("path")).getString("url"));
                    appversion.setPath(file);
                    if (compareVersion(appversion.getVersion_i())) {
                        if (new Config().ignoreVersion(appversion.getVersion())) {// 忽略该版
                            return;
                        }
                        if (new Config().getUpdateOnlyWifi()) {
                            // 判断当前是否wifi状态
//                            if (wifi){
//                                showUpdateDialog(appversion);
//                            }else {
//                                Toast.makeText(context, "发现新版本'"+appversion.getVersion()+"'可在Wi-Fi环境下更新，或手动检测新版", Toast.LENGTH_SHORT).show();
//                            }
                        } else {
                            showUpdateDialog(appversion);
                        }
                    } else {
                        if (b) {
                            Toast.makeText(context, "当前已是最新版本", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    if (b) {
                        if (object.toString().equals("response timeout")) {
                            Toast.makeText(context, "检测超时", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "似乎发生了什么错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void showUpdateDialog(final AppVersion appVersion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bmob_update_dialog, null);
        TextView textView = (TextView) view.findViewById(R.id.bmob_update_content);
        textView.setText(appVersion.getUpdate_log().replace("\\n", "\n"));
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.bmob_update_id_check);
        checkBox.setVisibility(appVersion.getIsforce() ? View.GONE : View.VISIBLE);
        builder.setTitle("发现新版本");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadApk(appVersion.getPath().getUrl());
            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkBox.isChecked()) {
                    new Config().setIgnoreVersion(appVersion.getVersion());
                }
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setView(view);
        dialog.show();
    }

    private void downloadApk(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle("正在下载" + context.getResources().getString(R.string.app_name) + "更新包");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileDir);
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        // 存储下载Key
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putLong("download_apk_id_prefs", manager.enqueue(request)).apply();
    }

    /**
     * compare the version between the version code.
     *
     * @param newVersionCode version code
     * @return true is the diff version or false is the same version.
     */
    private boolean compareVersion(int newVersionCode) {
        return getCurrentAppVersionCode() != newVersionCode;
    }

    /**
     * get current version code.
     *
     * @return current app version code.
     */
    private int getCurrentAppVersionCode() {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("BmobUpdateUtil", "getCurrentAppVersionCode: error: VersionCode is int type.");
            return 1;
        }
    }

    public String getFileDir() {
        return fileDir;
    }

    public BmobUpdateUtil setFileDir(String fileDir) {
        this.fileDir = fileDir;
        return this;
    }

    private class Config {
        private SharedPreferences sp = context.getSharedPreferences("appversion", Context.MODE_PRIVATE);

        private void setIgnoreVersion(String ignoreVersion) {
            sp.edit().putString("ignore", ignoreVersion).apply();
        }

        private boolean ignoreVersion(String version) {
            return version.equals(sp.getString("ignore", ""));
        }

        private void setUpdateOnlyWifi(boolean b) {
            sp.edit().putBoolean("onlywifi", b).apply();
        }

        private boolean getUpdateOnlyWifi() {
            return sp.getBoolean("onlywifi", false);
        }
    }

}
