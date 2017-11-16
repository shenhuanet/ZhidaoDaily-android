package com.shenhua.zhidaodaily.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.support.v4.view.WindowCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shenhua.zhidaodaily.R;
import com.shenhua.zhidaodaily.core.bean.DetailBean;
import com.shenhua.zhidaodaily.presenter.DetailPresenter;
import com.shenhua.zhidaodaily.utils.BaseWebViewClient;
import com.shenhua.zhidaodaily.utils.JsInterface;
import com.shenhua.zhidaodaily.view.DetailView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 详情页
 * <p>
 * Created by Shenhua on 12/10/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public class DetailActivity extends BaseActivity implements DetailView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_detail_photo)
    ImageView photoIv;
    @Bind(R.id.progress)
    ProgressBar progressBar;
    @Bind(R.id.web)
    WebView webView;
    private String dataUrl, title, imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_MODE_OVERLAY);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initAppbarLayout();
        initWebView();
        DetailPresenter presenter = new DetailPresenter(this);
        presenter.execute(dataUrl);
    }

    private void initAppbarLayout() {
        setupActionBar(toolbar, "", true);
        Intent intent = getIntent();
        dataUrl = intent.getStringExtra("dataUrl");
        title = intent.getStringExtra("title");
        imgUrl = intent.getStringExtra("img");

        System.out.println("shenhua sout:" + dataUrl + "  " + title + "  " + imgUrl);

        setupActionBarTitle(title);
        Glide.with(this).load(imgUrl).placeholder(R.drawable.daily_bg)
                .error(R.drawable.daily_bg).centerCrop().into(photoIv);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webView.addJavascriptInterface(new JsInterface(this), "imgClickListener");
        webView.setWebViewClient(new BaseWebViewClient());
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showDetail(DetailBean detail) {
        webView.loadDataWithBaseURL("", detail.getHtml(), "text/html", "UTF-8", "");
    }

    @Override
    public void showFail(final String msg) {
        Toast.makeText(DetailActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:

                break;
            case R.id.action_screenshot:

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Bitmap captureWebViewall(WebView webView) {
        Picture snapShot = webView.capturePicture();
        Bitmap bmp = Bitmap.createBitmap(snapShot.getWidth(),
                snapShot.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        snapShot.draw(canvas);
        return bmp;
    }

}
