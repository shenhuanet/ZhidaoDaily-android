package com.shenhua.zhidaodaily.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Activity 基类,提供Toolbar的配置和OptionMenu的配置
 * <p>
 * Created by shenhua on 12/1/2016.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    ActionBar ab;

    public void setupActionBar(Toolbar toolbar, int titleRes, boolean shouldBack) {
        this.setupActionBar(toolbar, getResources().getString(titleRes), shouldBack);
    }

    public void setupActionBar(Toolbar toolbar, String title, boolean shouldBack) {
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
            if (!shouldBack) {
                return;
            }
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setDisplayShowHomeEnabled(true);
        }

    }

    public void setupActionBarTitle(int titleRes) {
        setupActionBarTitle(getResources().getString(titleRes));
    }

    public void setupActionBarTitle(String title) {
        if (ab != null) {
            ab.setTitle(title);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition();
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
