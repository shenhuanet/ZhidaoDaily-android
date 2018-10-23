package com.shenhua.zhidaodaily.utils

import android.webkit.WebView
import android.webkit.WebViewClient


/**
 * Created by Shenhua on 12/9/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
class BaseWebViewClient : WebViewClient() {

    override fun shouldOverrideUrlLoading(webView: WebView?, s: String?): Boolean {
        webView!!.loadUrl(s)
        return true
    }

    override fun onPageFinished(webView: WebView?, s: String?) {
        super.onPageFinished(webView, s)
        webView!!.loadUrl("javascript:(function () {" +
                "    var objs = document.getElementsByTagName(\"img\");" +
                "    for (var i = 0; i < objs.length; i++) {" +
                "        objs[i].onclick = function () {" +
                "            window.imgClickListener.openImage(this.src);" +
                "        }" +
                "    }" +
                "})()")
    }
}
