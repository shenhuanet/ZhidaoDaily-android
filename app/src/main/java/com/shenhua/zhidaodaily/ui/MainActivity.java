package com.shenhua.zhidaodaily.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.shenhua.zhidaodaily.R;
import com.shenhua.zhidaodaily.presenter.HomePresenter;
import com.shenhua.zhidaodaily.update.BmobUpdateUtil;
import com.shenhua.zhidaodaily.utils.Constants;
import com.shenhua.zhidaodaily.utils.DailyAdapter;
import com.shenhua.zhidaodaily.view.HomeView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;

public class MainActivity extends BaseActivity implements HomeView {

    private static Boolean isExit = false;
    private boolean isInit;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
//    @Bind(R.id.progress)
//    ProgressBar progressBar;
    @Bind(R.id.empty)
    TextView empty;

    // TODO: 2/8/2017 6.0权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bmob.initialize(this, "8cb4ad0d5a449d6165997c181e736497");
        BmobInstallation.getCurrentInstallation().save();
        BmobUpdateUtil.getInstance(this).setFileDir("zhidaoDaily").updateAuto();

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupActionBar(toolbar, R.string.app_name, false);
        initView();
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

        // FirUpdater.getInstance().updateAuto(this, API_TAKEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isInit)
            doGetData();
        isInit = true;
    }

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
            startActivity(new Intent(this, SettingActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                progressBar.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                });
            }
        });
    }

    @Override
    public void hideProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                progressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void showData(final List datas) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DailyAdapter adapter = new DailyAdapter(MainActivity.this, datas);
                mRecyclerView.setAdapter(adapter);
                empty.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void showFail(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                empty.setVisibility(View.VISIBLE);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Timer tExit;
            if (!isExit) {
                isExit = true;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                tExit = new Timer();
                tExit.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }

                }, 2000);
            } else {
                this.finish();
                System.exit(0);
            }
        }
        return false;
    }

}
