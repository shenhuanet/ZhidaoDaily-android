package com.shenhua.zhidaodaily.utils;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by Shenhua on 12/9/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public class BaseWebViewClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String s) {
        webView.loadUrl(s);
        return true;
    }

    @Override
    public void onPageFinished(WebView webView, String s) {
        super.onPageFinished(webView, s);
        webView.loadUrl("javascript:(function () {" +
                "    var objs = document.getElementsByTagName(\"img\");" +
                "    for (var i = 0; i < objs.length; i++) {" +
                "        objs[i].onclick = function () {" +
                "            window.imgClickListener.openImage(this.src);" +
                "        }" +
                "    }" +
                "})()");
    }
}
