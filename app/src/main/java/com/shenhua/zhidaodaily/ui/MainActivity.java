package com.shenhua.zhidaodaily.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shenhua.zhidaodaily.App;
import com.shenhua.zhidaodaily.R;
import com.shenhua.zhidaodaily.presenter.HomePresenter;
import com.shenhua.zhidaodaily.utils.AppUtils;
import com.shenhua.zhidaodaily.utils.Constants;
import com.shenhua.zhidaodaily.view.DailyAdapter;
import com.shenhua.zhidaodaily.view.HomeView;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Shenhua on 12/10/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public class MainActivity extends BaseActivity implements HomeView {

    public static final int REQUEST_CODE_SETTING = 201;
    public static final int REQUEST_STORAGE_PERMISSION = 202;
    private volatile boolean isExit = false;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.empty)
    TextView mEmptyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppUtils.getInstance().getThemeConfig(this) ?
                AppCompatDelegate.MODE_NIGHT_YES :
                AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupActionBar(mToolbar, R.string.app_name, false);

        requestStoragePermission();

        initView();
    }

    /**
     * 请求读写存储卡权限
     */
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new PermissionDeclarationDialogFragment().show(getSupportFragmentManager(), "dialog");
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        }
    }

    private void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doGetData();
            }
        });

        doGetData();
    }

    /**
     * 获取数据开始,刷新数据
     */
    @OnClick(R.id.empty)
    void doGetData() {
        HomePresenter presenter = new HomePresenter(this);
        presenter.execute(Constants.DAILY_URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting) {
            startActivityForResult(new Intent(this, SettingActivity.class), REQUEST_CODE_SETTING);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void showData(ArrayList datas) {
        DailyAdapter adapter = new DailyAdapter(MainActivity.this, datas);
        mRecyclerView.setAdapter(adapter);
        mEmptyTv.setVisibility(View.GONE);
    }

    @Override
    public void showFail(final String msg) {
        mEmptyTv.setVisibility(View.VISIBLE);
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
                service.schedule(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            isExit = false;
                        }
                    }
                }, 2000, TimeUnit.MILLISECONDS);
            } else {
                isExit = true;
                this.finish();
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(mRecyclerView, "需要获取文件访问权限,否则该功能无法使用", Snackbar.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("--" + "  requestCode " + requestCode + "   resultCode:" + resultCode);
        if (requestCode == REQUEST_CODE_SETTING
                && resultCode == SettingActivity.CODE_CHANGE_SKIN) {
            getDelegate().setLocalNightMode(AppUtils.getInstance().getThemeConfig(this)
                    ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
            ((App) getApplication()).changeSkin = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("----== " + savedInstanceState);
    }
}