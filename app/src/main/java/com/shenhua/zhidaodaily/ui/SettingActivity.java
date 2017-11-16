package com.shenhua.zhidaodaily.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.shenhua.zhidaodaily.R;
import com.shenhua.zhidaodaily.utils.AppUtils;
import com.shenhua.zhidaodaily.utils.DataCleanManager;
import com.tencent.bugly.beta.Beta;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Shenhua on 12/10/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public class SettingActivity extends BaseActivity {

    public static final int CODE_CHANGE_SKIN = 202;
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
        AppCompatDelegate.setDefaultNightMode(AppUtils.getInstance().getThemeConfig(this) ?
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
        mThemeSwitch.setChecked(AppUtils.getInstance().getThemeConfig(this));
        mUpdateSwitch.setChecked(AppUtils.getInstance().getUpdateConfig(this));
        mVersionTv.setText(AppUtils.getInstance().getAppCurrentVersion(this));
    }

    /**
     * 更新缓存大小显示
     */
    private void updateCacheTextView() {
        mCacheTv.setText(DataCleanManager.getCacheSize(this));
    }

    @OnClick({R.id.rl_theme, R.id.rl_update, R.id.rl_cache, R.id.rl_check, R.id.switch_theme, R.id.switch_update})
    void clicks(View view) {
        switch (view.getId()) {
            case R.id.rl_theme:
                mThemeSwitch.setChecked(!mThemeSwitch.isChecked());
                changeSkin();
                break;
            case R.id.rl_update:
                mUpdateSwitch.setChecked(!mUpdateSwitch.isChecked());
                AppUtils.getInstance().setUpdateConfig(this, mUpdateSwitch.isChecked());
                break;
            case R.id.rl_cache:
                DataCleanManager.cleanCache(this);
                updateCacheTextView();
                Toast.makeText(this, "缓存清理成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_check:
                Beta.checkUpgrade(true, false);
                break;
            case R.id.switch_theme:
                changeSkin();
                break;
            case R.id.switch_update:
                AppUtils.getInstance().setUpdateConfig(this, mUpdateSwitch.isChecked());
                break;
            default:
                break;
        }
    }

    /**
     * 换肤
     */
    private void changeSkin() {
        AppUtils.getInstance().setThemeConfig(this, mThemeSwitch.isChecked());
        getDelegate().setLocalNightMode(mThemeSwitch.isChecked()
                ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
        recreate();
        Intent intent = new Intent();
        intent.putExtra("delete", true);
        this.setResult(CODE_CHANGE_SKIN, intent);
    }
}
