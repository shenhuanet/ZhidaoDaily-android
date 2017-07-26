package com.shenhua.zhidaodaily.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.shenhua.zhidaodaily.ui.PhotoActivity;

/**
 * 通用WebView JS interface
 * Created by Shenhua on 12/10/2016.
 * e-mail shenhuanet@126.com
 */
public class JSInterface {
    private Context context;

    public JSInterface(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void openImage(String url) {
        if (url != null && !TextUtils.isEmpty(url)) {
            if (url.endsWith("gif")) {
                Toast.makeText(context, "暂不能打开gif", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(context, PhotoActivity.class);
            intent.putExtra("imgUrl", url);
            context.startActivity(intent);
        }
    }
}
