package com.shenhua.zhidaodaily.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shenhua.zhidaodaily.R;
import com.shenhua.zhidaodaily.core.bean.DetailBean;
import com.shenhua.zhidaodaily.presenter.DetailPresenter;
import com.shenhua.zhidaodaily.utils.AppUtils;
import com.shenhua.zhidaodaily.utils.BaseWebViewClient;
import com.shenhua.zhidaodaily.utils.JsInterface;
import com.shenhua.zhidaodaily.utils.NestedScrollWebView;
import com.shenhua.zhidaodaily.view.DetailView;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    Toolbar mToolbar;
    @Bind(R.id.iv_detail_photo)
    ImageView mPhotoIv;
    @Bind(R.id.progress)
    ProgressBar mProgressBar;
    @Bind(R.id.webview)
    NestedScrollWebView mWebView;
    private String mDataUrl, mTitle, mImgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initAppbarLayout();
        initWebView();
        DetailPresenter presenter = new DetailPresenter(this);
        presenter.execute(mDataUrl);
    }

    private void initAppbarLayout() {
        setupActionBar(mToolbar, "", true);
        Intent intent = getIntent();
        mDataUrl = intent.getStringExtra("dataUrl");
        mTitle = intent.getStringExtra("title");
        mImgUrl = intent.getStringExtra("img");
        setupActionBarTitle(mTitle);
        Glide.with(this).load(mImgUrl).placeholder(R.drawable.daily_bg)
                .error(R.drawable.daily_bg).centerCrop().into(mPhotoIv);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setBlockNetworkImage(false);
        webSettings.setLoadsImagesAutomatically(true);
        mWebView.setLayerType(View.LAYER_TYPE_NONE, null);
        mWebView.addJavascriptInterface(new JsInterface(this), "imgClickListener");
        mWebView.addJavascriptInterface(new JsInterface(this), "comjs");
        mWebView.setWebViewClient(new BaseWebViewClient());
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showDetail(DetailBean detail) {
        mWebView.loadDataWithBaseURL("", detail.getHtml(), "text/html", "UTF-8", "");
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
            case R.id.action_share_text:
                mWebView.loadUrl("javascript:window.comjs.loadHtmlContent(document.documentElement.innerText);void(0)");
                break;
            case R.id.action_share_screenshot:
                // TODO: 2017-11-17-0017 capturePicture
                // 子线程截图,主线程分享图片
                // AppUtils.getInstance().shareImage(this, );
                break;
            case R.id.action_share_link:
                AppUtils.getInstance().shareText(this, "标题:" + mTitle + "\n" + "地址:" + mDataUrl);
                break;
            case R.id.action_save_image:
                // TODO: 2017-11-17-0017 saveImage
                Toast.makeText(this, "developing", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab)
    void share() {
        mToolbar.showOverflowMenu();
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
