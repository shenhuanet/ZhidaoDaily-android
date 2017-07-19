package com.shenhua.zhidaodaily.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.shenhua.lib.bmobupdater.BmobUpdateUtil;
import com.shenhua.zhidaodaily.R;
import com.shenhua.zhidaodaily.utils.AppUtils;
import com.shenhua.zhidaodaily.utils.DataCleanManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Shenhua on 12/10/2016.
 * e-mail shenhuanet@126.com
 */
public class SettingActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.switch_theme)
    Switch mThemeSwitch;
    @Bind(R.id.switch_update)
    Switch mUpdateSwitch;
    @Bind(R.id.tv_cache)
    TextView mCacheTv;
    @Bind(R.id.tv_version)
    TextView mVersionTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppUtils.getInstance(this).getThemeConfig() ?
                AppCompatDelegate.MODE_NIGHT_YES :
                AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setupActionBar(mToolbar, R.string.action_setting, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCacheTextView();
        mThemeSwitch.setChecked(AppUtils.getInstance(this).getThemeConfig());
        mUpdateSwitch.setChecked(AppUtils.getInstance(this).getUpdateConfig());
        mVersionTv.setText(AppUtils.getInstance(this).getAppCurrentVersion());
    }

    private void updateCacheTextView() {
        String cache = "0KB";
        try {
            cache = DataCleanManager.getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mCacheTv.setText(cache);
    }

    @OnClick({R.id.rl_theme, R.id.rl_update, R.id.rl_cache, R.id.rl_check, R.id.switch_theme, R.id.switch_update})
    void clicks(View view) {
        switch (view.getId()) {
            case R.id.rl_theme:
                mThemeSwitch.setChecked(!mThemeSwitch.isChecked());
                AppUtils.getInstance(this).setThemeConfig(mThemeSwitch.isChecked());
                getDelegate().setLocalNightMode(mThemeSwitch.isChecked() ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
                break;
            case R.id.rl_update:
                mUpdateSwitch.setChecked(!mUpdateSwitch.isChecked());
                AppUtils.getInstance(this).setUpdateConfig(mUpdateSwitch.isChecked());
                break;
            case R.id.rl_cache:
                DataCleanManager.clearAllCache(this);
                updateCacheTextView();
                Toast.makeText(this, "缓存清理成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_check:
                methodRequiresPermission();
                break;
            case R.id.switch_theme:
                AppUtils.getInstance(this).setThemeConfig(mThemeSwitch.isChecked());
                break;
            case R.id.switch_update:
                AppUtils.getInstance(this).setUpdateConfig(mUpdateSwitch.isChecked());
                break;
        }
    }

    @AfterPermissionGranted(1000)
    private void methodRequiresPermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            BmobUpdateUtil.getInstance(this).updateManual();
        } else {
            EasyPermissions.requestPermissions(this, "请赋予app相关权限", 1000, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int i, List<String> list) {
        // Already have permission, do the thing
        BmobUpdateUtil.getInstance(this).updateManual();
    }

    @Override
    public void onPermissionsDenied(int i, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            BmobUpdateUtil.getInstance(this).updateManual();
        }
    }
}
